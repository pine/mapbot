package moe.pine.mapbot.services.message;

import lombok.RequiredArgsConstructor;
import moe.pine.mapbot.google_map.GoogleMap;
import moe.pine.mapbot.medium.Medium;
import moe.pine.mapbot.medium.Place;
import moe.pine.mapbot.slack.TextField;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OutgoingMessageService {
    private final GoogleMap googleMap;
    private final Medium medium;
    private final UriExtractor uriExtractor;

    public List<TextField> generate(String incomingText) {
        List<String> urls = uriExtractor.extract(incomingText);
        if (CollectionUtils.isEmpty(urls)) {
            return List.of();
        }

        return urls.stream()
                .parallel()
                .flatMap(this::convertToPlace)
                .map(this::convertToMappedPlace)
                .map(this::convertToTextField)
                .toList();
    }

    private Stream<Place> convertToPlace(String absoluteUrl) {
        return medium.find(absoluteUrl).stream();
    }

    private MappedPlace convertToMappedPlace(Place place) {
        String query = String.format("%s (%s)", place.address(), place.name());
        String mapUrl = googleMap.generateSearchUrl(query);

        return new MappedPlace(
                place.name(),
                place.label(),
                place.address(),
                mapUrl
        );
    }

    private TextField convertToTextField(MappedPlace mappedPlace) {
        String messageText =
                String.format(":round_pushpin: <%s|%s>",
                        mappedPlace.mapUrl(),
                        mappedPlace.address());

        return new TextField(mappedPlace.label(), messageText);
    }
}
