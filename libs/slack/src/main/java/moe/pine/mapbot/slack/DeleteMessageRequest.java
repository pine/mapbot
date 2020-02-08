package moe.pine.mapbot.slack;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteMessageRequest {
    String channel;
    String ts;
}
