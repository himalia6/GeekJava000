package subthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 方法五：使用CyclicBarrier
 */
@Slf4j
public class Solution5 {
    public static void main(String[] args) {
        final int[] r = {1};

        final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                log.info("sum result r = {}", r[0]);
            }
        });

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        log.info("main thread waiting for result...");
    }
}
