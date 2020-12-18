package rpc.api;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import rpc.api.domain.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class RpcStubHelper {
    public static <S> S getService(Class<S> serviceClass, String endpoint) {
        final RpcStubHelperProxy<S> proxy = new RpcStubHelperProxy<>(serviceClass, endpoint);
        return (S) Proxy.newProxyInstance(RpcStubHelper.class.getClassLoader(), new Class[]{serviceClass}, proxy);
    }

    @Slf4j
    static class RpcStubHelperProxy<T> implements InvocationHandler {
        private Class<?> serviceClass;
        private String url;
        private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        public <S> RpcStubHelperProxy(Class<S> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
            final DummyRpcResponse<User>[] response = new DummyRpcResponse[]{null};
            try {
                final Bootstrap bootstrap = new Bootstrap()
                        .channel(NioSocketChannel.class)
                        .group(workerGroup)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline()
                                        .addLast("http", new HttpClientCodec())
                                        .addLast("aggregator", new HttpObjectAggregator(16 * 65536))
                                        .addLast("adapter", new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                if (msg instanceof HttpResponse) {
                                                    HttpResponse response = (HttpResponse) msg;
                                                    System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
                                                }
                                                if (msg instanceof HttpContent) {
                                                    HttpContent content = (HttpContent) msg;
                                                    ByteBuf buf = content.content();
                                                    response[0] = JSON.<DummyRpcResponse<User>>parseObject(buf.toString(CharsetUtil.UTF_8), DummyRpcResponse.class);
                                                    buf.release();
                                                }
                                            }

                                            @Override
                                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                                ctx.flush();
                                            }

                                            @Override
                                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                                super.exceptionCaught(ctx, cause);
                                            }
                                        });
                            }
                        });
                final ChannelFuture future =
                        bootstrap.connect("localhost", 8080).sync();
                final DummyRpcRequest rpcRequest = new DummyRpcRequest();
                rpcRequest.setParameters(new Object[]{"tom"});
                rpcRequest.setServiceClass(UserService.class);
                rpcRequest.setMethod("findByName");

                final DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                        HttpMethod.POST,
                        RpcStubHelperProxy.this.url,
                        Unpooled.wrappedBuffer(JSON.toJSONBytes(rpcRequest)));
                request.headers().set(HttpHeaderNames.HOST, "localhost:8080");
                request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

                future.channel().writeAndFlush(request).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                ;
                future.channel().closeFuture().sync();
                if (response.length > 0) {
                    return response[0].getResult();
                }
                return null;
            } finally {
//                workerGroup.shutdownGracefully();
            }
        }
    }
}
