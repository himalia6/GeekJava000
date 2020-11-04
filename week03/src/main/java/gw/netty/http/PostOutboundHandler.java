package gw.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

class ContentLengthHeaderRemover implements HttpRequestInterceptor {
    @Override
    public void process(org.apache.http.HttpRequest request, HttpContext httpContext) throws HttpException, IOException {
        request.removeHeaders(HTTP.CONTENT_LEN);// fighting org.apache.http.protocol.RequestContent's ProtocolException("Content-Length header already present");
    }
}

@Slf4j
public class PostOutboundHandler extends ChannelOutboundHandlerAdapter {
    private static final HttpClient httpClient;

    static {
        final HttpClientBuilder builder = HttpClients.custom();
        builder.addInterceptorFirst(new ContentLengthHeaderRemover());
        httpClient = builder.build();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            DefaultFullHttpResponse response = null;
            try {
                final RequestBuilder builder = RequestBuilder.create(request.method().name());
                for (Map.Entry<String, String> entry : request.headers().entries()) {
                    builder.setHeader(entry.getKey(), entry.getValue());
                }
                final HttpVersion httpVersion = request.protocolVersion();
                builder.setUri(request.headers().get("Full-Uri"));
                builder.setVersion(new ProtocolVersion(httpVersion.protocolName(), httpVersion.majorVersion(), httpVersion.minorVersion()));
                builder.setEntity(new ByteArrayEntity(request.content().array()));
                builder.setHeader(HTTP.TARGET_HOST, request.headers().get("host"));
                final HttpUriRequest uriRequest = builder.build();
                HttpResponse result = httpClient.execute(uriRequest);

                response = new DefaultFullHttpResponse(HttpVersion.valueOf(request.protocolVersion().text()),
                        HttpResponseStatus.valueOf(result.getStatusLine().getStatusCode()),
                        Unpooled.copiedBuffer(EntityUtils.toString(result.getEntity()).getBytes()));
                final HeaderIterator iterator = result.headerIterator();
                while (iterator.hasNext()) {
                    final Header header = iterator.nextHeader();
                    response.headers().set(header.getName(), header.getValue());
                }
                EntityUtils.consume(result.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    try {
                        if (HttpUtil.isKeepAlive(request)) {
                            response.headers().set(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
                            response.headers().set(HttpHeaderNames.CONTENT_LENGTH.toString(), String.valueOf(response.content().readableBytes()));
                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        } else {
                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ReferenceCountUtil.release(msg);
            }
        } else {
            ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
