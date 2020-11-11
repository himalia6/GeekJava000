package subthread;

import lombok.extern.slf4j.Slf4j;

/**
 * 方法二：使用volatile + loop
 */
@Slf4j
public class Solution2 {
    private static volatile boolean calculated = false;

    public static void main(String[] args) {
        final int[] r = {1};
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                    calculated = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        log.info("main thread waiting for result...");
        while (!calculated) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("sum result r = {}", r[0]);
    }
}
