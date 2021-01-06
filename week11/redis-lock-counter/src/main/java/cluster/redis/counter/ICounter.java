package cluster.redis.counter;

public interface ICounter {
    Long increment(long volume);

    Long decrement(long volume);

    Long get();
}
