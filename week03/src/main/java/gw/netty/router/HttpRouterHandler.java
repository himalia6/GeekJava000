package gw.netty.router;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpRouterHandler extends ChannelInboundHandlerAdapter {
    private final Map<String, String> routes;
    private List<PathMatcher> matchers = new ArrayList<>();

    public HttpRouterHandler(Map<String, String> routes) {
        this.routes = routes;
        matchers.add(new PrefixPathMatcher());
        matchers.add(new AntPathMatcher());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            final FullHttpRequest request = (FullHttpRequest) msg;
            final String url = request.uri();
            String target = getRoute(url);
            if (target != null) {
                request.setUri(target);
                request.headers().set("Auth-Token", "token-xxxxx");
                ReferenceCountUtil.retain(request);
                super.channelRead(ctx, request);
            } else {
                final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                if (HttpUtil.isKeepAlive(request)) {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                } else {
                    ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    private String getRoute(String path) {
        for (PathMatcher matcher : matchers) {
            for (String pattern : routes.keySet()) {
                if (matcher.matches(pattern, path)) {
                    return routes.get(pattern);
                }
            }
        }
        return null;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
