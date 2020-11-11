package subthread;

import lombok.extern.slf4j.Slf4j;

/**
 * 方法三：使用synchronized(lock)
 */
@Slf4j
public class Solution3 {
    public static void main(String[] args) {
        final Object lock = new Object();
        final int[] r = {1};

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        Thread.sleep(2000);
                        r[0] += 5;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
        log.info("main thread waiting for result...");

        synchronized (lock) {
            log.info("sum result r = {}", r[0]);
        }
    }
}
