package moe.pine.mapbot.log;

import lombok.Value;

@Value
public class SentLogId {
    private String channel;
    private String sourceTs;
}
