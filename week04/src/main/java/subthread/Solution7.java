package subthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 方法七：使用BlockingQueue/BlockingDeque等待或传递结果
 */
@Slf4j
public class Solution7 {
    public static void main(String[] args) {
//        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
//        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
//        final BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(1);
//        final BlockingQueue<Integer> queue = new LinkedTransferQueue<>();
//        final BlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        final BlockingQueue<Integer> queue = new SynchronousQueue<>();
        final int[] r = {1};

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    r[0] += 5;
                    queue.put(r[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        log.info("main thread waiting for result...");
        try {
            queue.take();
            log.info("sum result r = {}", r[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
