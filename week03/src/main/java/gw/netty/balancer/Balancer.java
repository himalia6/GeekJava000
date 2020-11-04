package gw.netty.balancer;

import java.net.URI;
import java.util.List;

public interface Balancer {
    String name();

    URI next();

    List<URI> targets();
}
