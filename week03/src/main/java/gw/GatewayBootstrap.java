package gw;


import gw.netty.server.GatewayServer;

public class GatewayBootstrap {
    public static void main(String[] args) {
        final GatewayServer server = new GatewayServer(8888, false);
        server.run();
    }
}
