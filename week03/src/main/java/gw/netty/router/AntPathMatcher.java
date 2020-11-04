package gw.netty.router;

import java.util.regex.Pattern;

public class AntPathMatcher implements PathMatcher {

    @Override
    public boolean matches(String pattern, String path) {
        final Pattern regex = getPattern(pattern);
        return regex.matcher(path).matches();
    }

    @Override
    public String replace(String pattern, String path) {
        final Pattern regex = getPattern(pattern);
        return regex.matcher(path).replaceFirst("/");
    }

    private Pattern getPattern(String pattern) {
        String patternRegex = pattern.replaceAll("\\*{2}", "(/?[^/]+/?)+");
        patternRegex = pattern.replaceAll("\\?", "[^/]");
        patternRegex = pattern.replaceAll("\\*", "[^/]+");
        return Pattern.compile(patternRegex);
    }
}
