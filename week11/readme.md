极客时间Java训练营第十一周作业
=========================


#### 一. 基于Redis封装分布式数据操作: 
* 1)在Java中实现一个简单的分布式锁
* 2)在Java中实现一个分布式计数器，模拟减库存

1) 简单分布式锁
原理，借助redis的setnx+setex / expire(del)的原理设置分布式锁，需要实现以下功能：

* 独占性，如果有记录则不操作，可以通过setnx完成
* lock用完后需要主动释放，这个通过调用del/expire(0)完成
* 需要能够应对异常情况下锁也能正常释放，这个通过setex完成
* 拿到锁的同时，需要同时原子性设置超时自动释放，即setnx和setex需要保证原子性，这里可以借助lua实现

代码大致如下[ClusterLocker.java](redis-lock-counter/src/main/java/cluster/redis/locks/ClusterLocker.java)：

lua代码实现原子性：
```java
    private static final String SCRIPT = "local key = KEYS[1];local val = KEYS[2];local expire = KEYS[3];"
            + "local result_1 = redis.call('SETNX', key, val);"
            + "if result_1 == 1 then "
            + "local result_2 = redis.call('SETEX', key, expire, val);"
            + "return result_2; else return result_1;end";
```
lock实现，通过eval执行lua脚本，传递参数获取结果，这里是blocking实现：
```java
   public void lock(final String key) {
        final List<String> keys = new ArrayList<>();
        keys.add(key);
        keys.add(environment.getProperty("server.port"));
        keys.add(TIMEOUT);
        Object result = jedis.eval(SCRIPT, keys, Collections.emptyList());
        while (!result.equals("OK")) {
            LockSupport.parkNanos(100 * 1000);
            result = jedis.eval(SCRIPT, keys, Collections.emptyList());
        }
    }
```
调用lock部分[LockerController.java](redis-lock-counter/src/main/java/cluster/redis/controller/LockerController.java)，
拿到redis锁的进程会将自己端口号写入lock的value并执行sleep，其他进程则会等待:
```java
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

```
2）分布式计数器

分布式计数器稍微简单一些，多个进程在更新计数使用同一个redis环境下的同一个key即可，可以通过调用
incrby(key)/decrby(key)或者incr/decr来实现。库存计数由于涉及数据库和redis缓存的同时更新，需要
同时借助分布式锁来实现。

分布式计数器代码大致如下[DistributedCounter.java](redis-lock-counter/src/main/java/cluster/redis/counter/DistributedCounter.java)
```java
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
```

调用部分如下[DistributedCounter.java](redis-lock-counter/src/main/java/cluster/redis/counter/DistributedCounter.java)：
```java
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
```