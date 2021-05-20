package moe.pine.mapbot.services.message;

public record MappedPlace(
        String name,
        String label,
        String address,
        String mapUrl
) {
}
