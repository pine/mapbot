package moe.pine.mapbot.medium;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Set;

@RequiredArgsConstructor
public class DenyHostFilter {
    private final Set<String> hosts;

    public boolean isDenied(String absoluteUrl) {
        URI uri = URI.create(absoluteUrl);
        return hosts.contains(uri.getHost());
    }
}
