package moe.pine.mapbot.slack;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * @see <a href="https://api.slack.com/methods/chat.update">chat.update method | Slack</a>
 */
@Value
@Builder
public class UpdateMessageRequest {
    String channel;
    String text;
    List<TextField> textFields;
    String ts;
}
