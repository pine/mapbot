package moe.pine.mapbot.medium;

import org.springframework.http.MediaType;

import java.util.Set;

public record CensoredMediaType(
        MediaType mediaType
) {
    private static final Set<MediaType> ALLOWED_MEDIA_TYPES = Set.of(
            MediaType.TEXT_HTML,
            MediaType.APPLICATION_XHTML_XML);

    public boolean isSupported() {
        return ALLOWED_MEDIA_TYPES.stream()
                .anyMatch(v -> v.getType().equals(mediaType.getType()) &&
                        v.getSubtype().equals(mediaType.getSubtype()));
    }
}
