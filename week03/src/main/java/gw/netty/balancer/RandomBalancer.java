package gw.netty.balancer;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

public class RandomBalancer implements Balancer{
    private static final String name = "Random";
    private final List<URI> targets;

    public RandomBalancer(List<URI> targets) {
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
        final byte[] bytes = new byte[4];
        new Random().nextBytes(bytes);
        final int index = ByteBuffer.wrap(bytes).getInt() % targets.size();
        return targets.get(index);
    }

    @Override
    public List<URI> targets() {
        return targets;
    }
}
