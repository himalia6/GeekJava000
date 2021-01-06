package cluster.redis.locks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Component
public class ClusterLocker implements ILocker {
    @Autowired
    private Jedis jedis;

    private static final String TIMEOUT = "30";

    private static final String SCRIPT = "local key = KEYS[1];local val = KEYS[2];local expire = KEYS[3];"
            + "local result_1 = redis.call('SETNX', key, val);"
            + "if result_1 == 1 then "
            + "local result_2 = redis.call('SETEX', key, expire, val);"
            + "return result_2; else return result_1;end";

    @Autowired
    private Environment environment;

    @Override
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

    @Override
    public boolean tryLock(final String key) {
        final List<String> keys = new ArrayList<>();
        keys.add("3");
        keys.add(key);
        keys.add("lock");
        keys.add(TIMEOUT);
        final Object result = jedis.eval(SCRIPT, keys, Collections.emptyList());
        return result.equals("OK");
    }

    @Override
    public void unlock(final String key) {
        jedis.expire(key, 0);
    }
}
