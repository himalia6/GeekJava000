package netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpInitializer extends ChannelInitializer<Channel> {
    private SslContext sslContext;

    public HttpInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast("codec", new HttpServerCodec());
        channel.pipeline().addLast("handler", new HttpHandler());
    }
}
