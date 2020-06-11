package moe.pine.mapbot.jsonld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
public class JsonLdParser {
    private final ObjectMapper objectMapper;
    private final Factory factory;

    public JsonLd parse(List<String> contents) {
        List<? extends Thing> things =
                contents.stream()
                        .flatMap(content -> parse(content).stream())
                        .collect(Collectors.toUnmodifiableList());
        return new JsonLd(things);
    }

    protected List<? extends Thing> parse(String content) {
        if (StringUtils.isBlank(content)) {
            return List.of();
        }

        try {
            JsonNode node = objectMapper.readTree(content);
            Stream<JsonNode> childNodes;
            if (node.isArray()) {
                childNodes = StreamSupport.stream(node.spliterator(), false);
            } else {
                childNodes = Stream.of(node);
            }

            return childNodes
                    .flatMap(childNode ->
                            factory.getCreators()
                                    .stream()
                                    .flatMap(creator -> creator.create(childNode, factory).stream())
                                    .limit(1))
                    .collect(Collectors.toUnmodifiableList());
        } catch (JsonProcessingException e) {
            UncheckedIOException re = new UncheckedIOException(e);
            log.error("Unable to parse JSON-LD. [content={}]", content, re);
        }

        return List.of();
    }
}
