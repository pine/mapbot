package moe.pine.mapbot.medium;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class HostFilter {
    private final Set<String> deniedHosts;

    public HostFilter(Set<String> deniedHosts) {
        log.info("Denied hosts: {}", deniedHosts);

        this.deniedHosts = Objects.requireNonNull(deniedHosts);
    }

    public boolean isDenied(String absoluteUrl) {
        URI uri = URI.create(absoluteUrl);
        return deniedHosts.contains(uri.getHost());
    }
}
