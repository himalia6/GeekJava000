package cluster.redis.counter;

import redis.clients.jedis.Jedis;

public class DistributedCounter implements ICounter {
    private final String counter;

    private final Jedis jedis;

    public DistributedCounter(String counter, Jedis jedis) {
        this.counter = counter;
        this.jedis = jedis;
    }

    @Override
    public Long increment(long volume) {
        return this.jedis.incrBy(this.counter, volume);
    }

    @Override
    public Long decrement(long volume) {
        return this.jedis.decrBy(this.counter, volume);
    }

    @Override
    public Long get() {
        return Long.valueOf(this.jedis.get(this.counter));
    }


}
