package subthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 方法八：使用ExecutorService + FutureTask.get
 */
@Slf4j
public class Solution8 {
    public static void main(String[] args) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final int[] r = {1};

        final FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(2000);
                r[0] += 5;
                return r[0];
            }
        });

        executor.submit(task);

        log.info("main thread waiting for result...");
        try {
            task.get();
            log.info("sum result r = {}", r[0]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // necessary to exit from main thread
        executor.shutdown();
    }
}
