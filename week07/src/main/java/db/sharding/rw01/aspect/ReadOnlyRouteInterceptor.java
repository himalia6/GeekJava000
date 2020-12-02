package db.sharding.rw01.aspect;

import db.sharding.rw01.config.RoutingDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
