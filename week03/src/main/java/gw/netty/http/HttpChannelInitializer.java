package gw.netty.http;

import gw.netty.router.HttpRouterHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import gw.netty.balancer.BalancerType;
import gw.netty.balancer.LoadBalancerHandler;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpChannelInitializer extends ChannelInitializer {
    private final SslContext ssc;

    public HttpChannelInitializer(SslContext ssc) {
        this.ssc = ssc;
    }

    protected void initChannel(Channel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        if (ssc != null) {
            pipeline.addLast(ssc.newHandler(channel.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new PostOutboundHandler());
        pipeline.addLast(new PreInBoundHandler());
        Map<String, String> routes = new HashMap<>();
        routes.putIfAbsent("/bing/", "https://www.bing.com");
        routes.putIfAbsent("/baidu/**", "https://www.baidu.com");
        routes.putIfAbsent("/zhihu/**", "https://www.zhihu.com");
        pipeline.addLast(new HttpRouterHandler(routes));
        pipeline.addLast(new LoadBalancerHandler(
                Arrays.asList(URI.create("https://cn.bing.com/"), URI.create("https://www.baidu.com"), URI.create("https://www.zhihu.com")),
                BalancerType.ROUND_ROBIN));
        log.info("channel initialized.");
    }
}
