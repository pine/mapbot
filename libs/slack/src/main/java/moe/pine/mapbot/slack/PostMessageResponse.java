package moe.pine.mapbot.slack;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostMessageResponse {
    String channel;
    String ts;
}
