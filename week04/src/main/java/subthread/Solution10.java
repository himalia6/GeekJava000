package subthread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 方法十：使用ServerSocket/Socket方式确认计算完成
 */
@Slf4j
public class Solution10 {
    public static void main(String[] args) {
        final int[] r = {1};

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                    final Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(9000));
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        log.info("main thread waiting for result...");
        try {
            final ServerSocket serverSocket = new ServerSocket(9000);
            try(final Socket socket = serverSocket.accept()) {
                log.info("sum result r = {}", r[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
