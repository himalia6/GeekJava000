package gw.netty.router;

public interface PathMatcher {
    boolean matches(String pattern, String path);

    String replace(String pattern, String path);
}
