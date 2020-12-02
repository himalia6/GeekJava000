package db.sharding.rw01.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

    @Bean("primary")
    @ConfigurationProperties(prefix = "spring.primary.datasource")
    public DataSourceProperties primary() {
        return new DataSourceProperties();
    }

    @Bean("replica1")
    @ConfigurationProperties(prefix = "spring.replica1.datasource")
    public DataSourceProperties replica1() {
        return new DataSourceProperties();
    }
}
