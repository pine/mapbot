package moe.pine.mapbot.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class SentLog {
    String channel;
    String sourceTs;
    String destinationTs;
}
