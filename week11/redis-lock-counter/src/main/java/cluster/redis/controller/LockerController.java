package cluster.redis.controller;

import cluster.redis.locks.ClusterLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/lockers")
@Slf4j
public class LockerController {

    @Autowired
    private ClusterLocker locker;

    @GetMapping("/acquire/{key}")
    public String accquire(@PathVariable("key") final String key) {
        // may wait forever
        if (key != null && !key.equals("")) {
            try {
                locker.lock(key);
                log.info("got locker, starting work...");
                Thread.sleep(Math.abs(new Random().nextLong()) % 20000);
                log.info("finishing work...");
                return "Happy working with lock";
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                locker.unlock(key);
                log.info("released lock...");
            }
        }
        return "Key Error";
    }

}
