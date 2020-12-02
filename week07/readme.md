极客时间Java训练营第七周作业
=========================


#### 1. 按自己设计的表结构，插入100万订单模拟数据，测试不同的方式的插入效率。
- Java代码建立SQL会话插入
- 执行SQL脚本
- 使用binlog




#### 2. 读写分离-动态切换数据源版本1.0
这种方式实现的目标是，在读操作之前，切换到读数据源。借助方法AOP切换源，AbstractRoutingDataSource
从读写源中获取只读源，主要代码如下：
[代码目录](src/main/java/db/sharding/rw01)

1- 实现AbstractRoutingDataSource#determineCurrentLookupKey方法，使用key根据上下文环境获取数据源：
```java
public class RoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<Route> ctx = new ThreadLocal<>();

    public static void switchToReplicaRoute() {
        ctx.set(Route.REPLICA);
    }

    public static void switchBack() {
        ctx.remove();
    }

    public enum Route {
        PRIMARY, REPLICA
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ctx.get();
    }
}
```
2- 绑定Key和多个读写数据源之间的关系
```java
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
```
3- 在读操作入口切换上下文中key，读结束后重置回：
```java
@Aspect
@Component
@Order(0)
public class ReadOnlyRouteInterceptor {

    @Around(value = "@annotation(tx)")
    public Object proceed(ProceedingJoinPoint pjp, Transactional tx) throws Throwable {
        try {
            if (tx.readOnly()) {
                RoutingDataSource.switchToReplicaRoute();
            }
            return pjp.proceed();
        } finally {
            RoutingDataSource.switchBack();
        }
    }
}
```
由上述代码可以看出，此种方式对业务代码侵入是比较大的。

#### 3. 读写分离-动态切换数据源版本2.0 




#### 4. 读写分离-数据库中间件版本3.0 