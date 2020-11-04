package gw.netty.balancer;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

@Slf4j
public class LoadBalancerFactory {

    public static Balancer getBalancer(BalancerType balancerType, List<URI> targets) {
        switch (balancerType) {
            case ROUND_ROBIN:
                return new RoundRobinBalancer(targets);
            case RANDOM:
            default:
                return new RandomBalancer(targets);
        }
    }
}

