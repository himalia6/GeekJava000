package db.sharding.rw01.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class PrimaryDataSource {
    @Autowired
    @Qualifier("primary")
    private DataSourceProperties primary;

    @Autowired
    @Qualifier("replica1")
    private DataSourceProperties replica1;

    @Primary
    @Bean("primarySource")
    public DataSource dataSource() {
        final RoutingDataSource dataSource = new RoutingDataSource();

        final DataSource primarySource = buildDataSource(primary);
        final DataSource replica1Source = buildDataSource(replica1);

        final HashMap<Object, Object> targets = new HashMap<>();
        targets.put(RoutingDataSource.Route.PRIMARY, primarySource);
        targets.put(RoutingDataSource.Route.REPLICA, replica1Source);
        dataSource.setTargetDataSources(targets);
        dataSource.setDefaultTargetDataSource(primarySource);

        return dataSource;
    }

    private DataSource buildDataSource(DataSourceProperties properties) {
        final HikariConfig config = new HikariConfig();
        config.setPoolName(properties.getPoolName());
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriver());
        config.setReadOnly(properties.isReadOnly());
        return new HikariDataSource(config);
    }

}
