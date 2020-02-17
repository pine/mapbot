package moe.pine.mapbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.google_map.GoogleMap;
import moe.pine.mapbot.models.MappedPlace;
import moe.pine.mapbot.place.Place;
import moe.pine.mapbot.properties.SlackProperties;
import moe.pine.mapbot.slack.MessageEvent;
import moe.pine.mapbot.slack.SlackClient;
import moe.pine.mapbot.tabelog.Tabelog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSentEventHandler {
    private final SlackProperties slackProperties;
    private final SlackClient slackClient;
    private final Tabelog tabelog;
    private final UriExtractor uriExtractor;
    private final GoogleMap googleMap;

    void execute(MessageEvent messageEvent) throws InterruptedException {
        String text = messageEvent.getText();
        List<String> urls = uriExtractor.extract(text);
        if (CollectionUtils.isEmpty(urls)) {
            return;
        }

        List<MappedPlace> mappedPlaces =
                urls.stream()
                        .parallel()
                        .flatMap(url -> {
                            Optional<Place> place = tabelog.find(url);
                            return place.stream();
                        })
                        .map(place -> new MappedPlace(
                                place.getName(),
                                googleMap.generateSearchUrl(place.getAddress())))
                        .collect(Collectors.toUnmodifiableList());
        if (CollectionUtils.isEmpty(mappedPlaces)) {
            return;
        }

        log.debug("{}", mappedPlaces);
    }
}
