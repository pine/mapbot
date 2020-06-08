package moe.pine.mapbot.jsonld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.jsonld.factory.Factory;
import moe.pine.mapbot.jsonld.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JsonLdParser {
    private final ObjectMapper objectMapper;
    private final Factory factory;

    public JsonLd parse(List<String> contents) {
        List<Thing> things =
                contents.stream()
                        .flatMap(content -> parse(content).stream())
                        .collect(Collectors.toUnmodifiableList());
        return new JsonLd(things);
    }

    protected List<Thing> parse(String content) {
        if (StringUtils.isBlank(content)) {
            return List.of();
        }

        try {
            JsonNode node = objectMapper.readTree(content);
            if (node.isArray()) {

            } else {
                return factory.getCreators()
                        .stream()
                        .flatMap(creator -> creator.create(node, factory).stream())
                        .limit(1)
                        .collect(Collectors.toUnmodifiableList());
            }
        } catch (JsonProcessingException e) {
        }

        /*
        try {
            Thing thing = objectMapper.readValue(content, Thing.class);
            return List.of(thing);
        } catch (JsonProcessingException e1) {
            try {
                return objectMapper.readValue(content, new TypeReference<>() {
                });
            } catch (JsonProcessingException e2) {
                UncheckedIOException re = new UncheckedIOException(e2);
                re.addSuppressed(e1);
                log.error("Unable to parse JSON-LD. [content={}]", content, re);
            }
        }

         */

        return List.of();
    }
}
