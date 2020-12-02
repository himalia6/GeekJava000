package db.sharding.rw01.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
