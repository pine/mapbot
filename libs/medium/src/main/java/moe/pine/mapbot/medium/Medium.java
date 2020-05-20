package moe.pine.mapbot.medium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.amp.Amp;
import moe.pine.mapbot.jsonld.JsonLd;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class Medium {
    private final Amp amp;
    private final JsonLd jsonLd;

    public Optional<Place> find(String absoluteUrl) {
        Optional<String> redirectedUrl = amp.resolveRedirect(absoluteUrl);
        log.debug("AMP resolved URL: {}", redirectedUrl);

        String resolvedUrl = redirectedUrl.orElse(absoluteUrl);
        log.debug("Resolved URL: {}", resolvedUrl);

        return Optional.empty();
    }
}
