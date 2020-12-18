package db.sharding.rw02.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.ReplicaQueryRuleConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.rule.ReplicaQueryDataSourceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Configuration
public class ShardingSphereDataSourceConfiguration {

    @Primary
    @Bean("primarySource")
    public DataSource createDataSource() throws SQLException {
        return ShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(),
                Arrays.asList(createShardingRuleConfiguration(), createReplicaQueryRuleConfiguration()),
                getProperties());
    }

    private ReplicaQueryRuleConfiguration createReplicaQueryRuleConfiguration() {
        final ReplicaQueryDataSourceRuleConfiguration configuration = new ReplicaQueryDataSourceRuleConfiguration(
                "ds_0", "book_ds_0", Collections.singletonList("book_ds_0_replica_0"), null);
        return new ReplicaQueryRuleConfiguration(Collections.singleton(configuration), Collections.EMPTY_MAP);
    }

    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getTables().add(getBookTableRuleConfiguration());
        result.getBindingTableGroups().add("book");
        result.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", "inline"));
        Properties props = new Properties();
//        props.setProperty("algorithm-expression", "book_ds_${id % 2}");
        result.getShardingAlgorithms().put("inline", new ShardingSphereAlgorithmConfiguration("INLINE", props));
        result.getKeyGenerators().put("snowflake", new ShardingSphereAlgorithmConfiguration("SNOWFLAKE", getProperties()));
        return result;
    }

    private static ShardingTableRuleConfiguration getBookTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("book", "ds_0.book");
        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("id", "snowflake"));
        return result;
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("book_ds_0", createDataSource("book_ds_0", "localhost", 33060, "root", "root"));
        result.put("book_ds_0_replica_0", createDataSource("book_ds_1", "localhost", 33061, "root", "root"));
        return result;
    }

    private static Properties getProperties() {
        Properties result = new Properties();
        result.setProperty("sql-show", "true");
        return result;
    }

    private DataSource createDataSource(final String dataSourceName,
                                        final String host,
                                        final Integer port,
                                        final String username,
                                        final String password) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName("com.mysql.jdbc.Driver");
        result.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8",
                host, port, dataSourceName));
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }
}
