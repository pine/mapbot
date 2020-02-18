package moe.pine.mapbot.services;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.google_map.GoogleMap;
import moe.pine.mapbot.models.MappedPlace;
import moe.pine.mapbot.place.Place;
import moe.pine.mapbot.tabelog.Tabelog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutgoingTextService {
    private final UriExtractor uriExtractor;
    private final Tabelog tabelog;
    private final GoogleMap googleMap;

    public Optional<String> generate(String incomingText) {
        List<String> urls = uriExtractor.extract(incomingText);
        if (CollectionUtils.isEmpty(urls)) {
            return Optional.empty();
        }

        String outgoingText =
                urls.stream()
                        .parallel()
                        .flatMap(this::convertToPlace)
                        .map(this::convertToMappedPlace)
                        .flatMap(this::convertToText)
                        .collect(Collectors.joining("\n"));

        return Optional.ofNullable(StringUtils.firstNonEmpty(outgoingText));
    }

    @VisibleForTesting
    Stream<Place> convertToPlace(String url) {
        return tabelog.find(url).stream();
    }

    @VisibleForTesting
    MappedPlace convertToMappedPlace(Place place) {
        return MappedPlace.builder()
                .name(place.getName())
                .address(place.getAddress())
                .mapUrl(googleMap.generateSearchUrl(place.getAddress()))
                .build();
    }

    @VisibleForTesting
    Stream<String> convertToText(MappedPlace mappedPlace) {
        String messageText = ":round_pushpin: <" + mappedPlace.getMapUrl() + "|" + mappedPlace.getName() + ">";
        log.debug("{}", messageText);

        return Stream.of(messageText);
    }
}
