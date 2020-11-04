package gw.netty.http.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpClient {
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void handleRequest(FullHttpRequest request, ChannelHandlerContext ctx) {

    }
}
