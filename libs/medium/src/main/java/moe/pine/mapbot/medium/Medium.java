package moe.pine.mapbot.medium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.amp.Amp;
import moe.pine.mapbot.jsonld.JsonLd;
import moe.pine.mapbot.jsonld.types.PostalAddress;
import moe.pine.mapbot.jsonld.types.Restaurant;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class Medium {
    private final Amp amp;
    private final Browser browser;

    public Optional<Place> find(String absoluteUrl) {
        Optional<String> originalUrlOpt = amp.resolveOriginalUrl(absoluteUrl);
        log.debug("AMP original URL: {}", originalUrlOpt);

        String resolvedUrl = originalUrlOpt.orElse(absoluteUrl);
        log.debug("Resolved URL: {}", resolvedUrl);

        Optional<Browser.Context> contextOpt = browser.browse(resolvedUrl);
        if (contextOpt.isEmpty()) {
            log.info("Unable to get the content. [absolute-url={}, resolved-url={}]", absoluteUrl, resolvedUrl);
            return Optional.empty();
        }

        Browser.Context context = contextOpt.get();
        OgpMetadata ogpMetadata = browser.getOgpMetadata(context);
        if (StringUtils.isBlank(ogpMetadata.getTitle())) {
            log.info("OGP title not found. [absolute-url={}, resolved-url={}]", absoluteUrl, resolvedUrl);
            return Optional.empty();
        }

        JsonLd jsonLd = browser.getEmbeddedJsonLd(context);
        Optional<Restaurant> restaurantOpt = jsonLd.findAny(Restaurant.class);
        if (restaurantOpt.isEmpty()) {
            log.info("Unable to find any restaurant in the page. [absolute-url={}, resolved-url={}]", absoluteUrl, resolvedUrl);
            return Optional.empty();
        }

        Restaurant restaurant = restaurantOpt.get();
        String name = restaurant.getName();
        if (StringUtils.isBlank(name)) {
            log.info("A restaurant name not found. [absolute-url={}, restaurant={}]", absoluteUrl, restaurant);
            return Optional.empty();
        }

        String address = Optional.ofNullable(restaurant.getAddress())
                .map(PostalAddress::getDomesticAddress)
                .orElse(StringUtils.EMPTY);
        if (StringUtils.isBlank(address)) {
            log.info("Address not found. [absolute-url={}, restaurant={}]", absoluteUrl, restaurant);
            return Optional.empty();
        }

        Place place = new Place(name, ogpMetadata.getTitle(), address);
        return Optional.of(place);
    }
}
