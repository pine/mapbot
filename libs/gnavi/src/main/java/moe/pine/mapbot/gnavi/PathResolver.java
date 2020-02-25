package moe.pine.mapbot.gnavi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

@Slf4j
class PathResolver {
    private static final String OFFICIAL_URL_PREFIX = "https://r.gnavi.co.jp/";

    Optional<String> resolve(String absoluteUrl) {
        Objects.requireNonNull(absoluteUrl);

        if (absoluteUrl.startsWith(OFFICIAL_URL_PREFIX)) {
            return Optional.ofNullable(StringUtils.firstNonEmpty(getPath(absoluteUrl)));
        }

        return Optional.empty();
    }

    private String getPath(String absoluteUrl) {
        try {
            return new URL(absoluteUrl).getPath();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
}
