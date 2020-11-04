package gw.netty.server;

import gw.netty.http.HttpChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class GatewayServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(GatewayServer.class);
    private final int port;
    private final boolean ssl;

    public GatewayServer(int port, boolean ssl) {
        this.port = port;
        this.ssl = ssl;
    }

    public void run() {
        SslContext sslContext = null;
        if (ssl) {
            try {
                final SelfSignedCertificate certificate = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey())
                        .sslProvider(SslProvider.JDK).build();
            } catch (CertificateException | SSLException e) {
                LOGGER.error("cert error => {}", e.getMessage());
            }
        }

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO));

            final ChannelFuture future = bootstrap
                    .childHandler(new HttpChannelInitializer(sslContext))
                    .group(bossGroup, workerGroup)
                    .bind(port).sync();

            LOGGER.info("server started on binding address: {}",
                    String.format("%s://127.0.0.1:%s/", ssl ? "https" : "http", port));

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
