package subthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * 方法六：使用Semaphore信号量
 */
@Slf4j
public class Solution6 {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(1);
        final int[] r = {1};

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    Thread.sleep(2000);
                    r[0] += 5;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        });

        thread.start();
        log.info("main thread waiting for result...");
        try {
            Thread.sleep(1);
            semaphore.acquire();
            log.info("sum result r = {}", r[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
