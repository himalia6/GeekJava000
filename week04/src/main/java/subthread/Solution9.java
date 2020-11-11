package subthread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 方法九：通过ThreadGroup.activeCount()<=2来判定
 */
@Slf4j
public class Solution9 {
    public static void main(String[] args) {
        final int[] r = {1};

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        log.info("main thread waiting for result...");
        while (Thread.currentThread().getThreadGroup().activeCount() > 2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("sum result r = {}", r[0]);
    }
}
