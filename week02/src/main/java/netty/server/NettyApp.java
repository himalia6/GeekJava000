package netty.server;

public class NettyApp {
    public static void main(String[] args) {
        final HttpServer server = new HttpServer(8888,true);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
