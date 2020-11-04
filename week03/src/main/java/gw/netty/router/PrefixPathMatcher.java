package gw.netty.router;

public class PrefixPathMatcher implements PathMatcher {

    @Override
    public boolean matches(String pattern, String path) {
        return path.startsWith(pattern);
    }

    @Override
    public String replace(String pattern, String path) {
        return path.replaceFirst(pattern, "/");
    }
}
