package subthread;

import lombok.extern.slf4j.Slf4j;

/**
 * 方法一：使用线程start+join
 */
@Slf4j
public class Solution1 {
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
        try {
            thread.join();
            log.info("sum result r = {}", r[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
