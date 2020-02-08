package moe.pine.mapbot.slack;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PostMessageRequest {
    String username;
    String threadTs;
    String channel;
    List<TextField> textFields;
    String iconUrl;
    boolean replyBroadcast;
}
