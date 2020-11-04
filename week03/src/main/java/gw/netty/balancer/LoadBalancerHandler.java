package gw.netty.balancer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

@Slf4j
public class LoadBalancerHandler extends ChannelInboundHandlerAdapter {
    private static Balancer balancer;

    public LoadBalancerHandler(List<URI> targets, BalancerType balancerType) {
        balancer = LoadBalancerFactory.getBalancer(balancerType, targets);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final FullHttpRequest request = (FullHttpRequest) msg;
        try {
            final URI uri = balancer.next();
            request.setUri(uri.getPath());
            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
            request.headers().set("Full-Uri", uri.toURL().toString());
            ctx.write(request);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
