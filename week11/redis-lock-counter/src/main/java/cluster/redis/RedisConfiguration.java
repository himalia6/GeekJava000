package cluster.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfiguration {

    @Bean
    public Jedis createJedis() {
        final Jedis jedis = new Jedis("localhost", 63790);
        return jedis;
    }
}
