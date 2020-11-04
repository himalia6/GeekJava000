package gw.netty.balancer;

import java.net.URI;
import java.util.List;

public class RoundRobinBalancer implements Balancer {
    private static final String name = "Random";
    private static int lastId = 0;
    private final List<URI> targets;

    public RoundRobinBalancer(List<URI> targets) {
        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException("empty targeting hosts");
        }
        this.targets = targets;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public URI next() {
        URI next;
        synchronized (RoundRobinBalancer.class) {
            next = targets.get(lastId);
            lastId = (lastId + 1) % targets.size();
        }
        return next;
    }

    @Override
    public List<URI> targets() {
        return targets;
    }

}
