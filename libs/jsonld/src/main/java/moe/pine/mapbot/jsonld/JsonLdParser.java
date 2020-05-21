package moe.pine.mapbot.jsonld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.mapbot.jsonld.types.Thing;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JsonLdParser {
    private final ObjectMapper objectMapper;

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

        return List.of();
    }
}
