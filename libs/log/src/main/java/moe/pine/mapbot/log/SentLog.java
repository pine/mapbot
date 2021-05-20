package moe.pine.mapbot.log;

public record SentLog(
        String channel,
        String sourceTs,
        String destinationTs
) {
}
