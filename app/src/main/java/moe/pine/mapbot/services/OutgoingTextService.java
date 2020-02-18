package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.google_map.GoogleMap;
import moe.pine.mapbot.models.MappedPlace;
import moe.pine.mapbot.place.Place;
import moe.pine.mapbot.slack.TextField;
import moe.pine.mapbot.tabelog.Tabelog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutgoingTextService {
    private final UriExtractor uriExtractor;
    private final Tabelog tabelog;
    private final GoogleMap googleMap;

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
                .collect(Collectors.toUnmodifiableList());
    }

    private Stream<Place> convertToPlace(String url) {
        return tabelog.find(url).stream();
    }

    private MappedPlace convertToMappedPlace(Place place) {
        String query = String.format("%s (%s)", place.getAddress(), place.getName());
        String mapUrl = googleMap.generateSearchUrl(query);

        return MappedPlace.builder()
                .name(place.getName())
                .label(place.getLabel())
                .address(place.getAddress())
                .mapUrl(mapUrl)
                .build();
    }

    private TextField convertToTextField(MappedPlace mappedPlace) {
        String messageText =
                String.format(":round_pushpin: <%s|%s>",
                        mappedPlace.getMapUrl(),
                        mappedPlace.getAddress());

        return new TextField(mappedPlace.getLabel(), messageText);
    }
}
