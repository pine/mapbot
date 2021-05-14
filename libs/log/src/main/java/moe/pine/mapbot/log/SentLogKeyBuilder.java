package moe.pine.mapbot.log;

import java.util.Objects;

class SentLogKeyBuilder {
    private static final String KEY_FORMAT = "sent_log:%s:%s";

    String buildKey(SentLogId sentLogId) {
        Objects.requireNonNull(sentLogId, "`sentLogId` is required.");
        Objects.requireNonNull(sentLogId.channel(), "`channel` is required.");
        Objects.requireNonNull(sentLogId.sourceTs(), "`sourceTs` is required.");

        return String.format(KEY_FORMAT, sentLogId.channel(), sentLogId.sourceTs());
    }
}
