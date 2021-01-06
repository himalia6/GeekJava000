package cluster.redis.locks;

public interface ILocker {
    void lock(final String key);

    boolean tryLock(final String key);

    void unlock(final String key);
}
