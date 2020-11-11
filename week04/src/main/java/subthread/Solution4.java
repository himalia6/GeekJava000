package subthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * 方法四：使用CountDownLatch
 */
@Slf4j
public class Solution4 {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);

        final int[] r = {1};
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });

        thread.start();
        log.info("main thread waiting for result...");
        try {
            latch.await();
            log.info("sum result r = {}", r[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
