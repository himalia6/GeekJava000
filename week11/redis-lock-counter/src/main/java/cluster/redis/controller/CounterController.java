package cluster.redis.controller;

import cluster.redis.counter.DistributedCounter;
import cluster.redis.locks.ClusterLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import redis.clients.jedis.Jedis;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/counters")
public class CounterController {
    @Autowired
    private Jedis jedis;

    @Autowired
    private ClusterLocker locker;

    @GetMapping("/{counter}")
    public Long getCounter(@PathVariable("counter") final String counter) {
        final DistributedCounter distributedCounter = new DistributedCounter(counter, jedis);
        return distributedCounter.get();
    }

    /**
     * incr or decr an counter
     *
     * @param counter counter name
     * @param method  incr/decr
     * @param volume  volume to update
     * @return
     */
    @PostMapping("/{counter}")
    public Long updateCounter(@PathVariable("counter") final String counter,
                              @PathParam("method") final String method,
                              @PathParam("volume") final Long volume) {
        final String storeKey = "store-item-storeKey";
        try {
            locker.lock(storeKey);
            final DistributedCounter distributedCounter = new DistributedCounter(counter, jedis);
            Long delta = volume;
            if (delta == null) delta = 1L;
            if ("incr".equalsIgnoreCase(method)) {
                // update db incr +
                return distributedCounter.increment(delta);
            } else if ("decr".equalsIgnoreCase(method)) {
                // update db decr -
                return distributedCounter.decrement(delta);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
        } finally {
            locker.unlock(storeKey);
        }
    }
}
