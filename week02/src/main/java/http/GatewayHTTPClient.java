package http;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GatewayHTTPClient {
    static HttpProxyClient client = new HttpProxyClient();

    public static void main(String[] args) {
        final ExecutorService pool = Executors.newFixedThreadPool(40);
        try (final ServerSocket serverSocket = new ServerSocket(8805)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                pool.execute(() -> {
                        service(socket);
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type:text/html;charset=utf-8");
            final long start = System.currentTimeMillis();
            String body = client.request(new HttpGet("http://localhost:8801"));
            final long duration = System.currentTimeMillis() - start;
            writer.println("Content-Length:" + body.getBytes().length);
            // request remote time
            writer.println("Request-Time:" + duration);
            writer.println();
            writer.write(body);
            writer.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
