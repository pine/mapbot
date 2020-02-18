package moe.pine.mapbot.log;

import lombok.Value;

@Value
public class SentLogId {
    String channel;
    String sourceTs;
}
