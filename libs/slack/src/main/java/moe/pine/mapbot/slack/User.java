package moe.pine.mapbot.slack;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String id;
    String realName;
    String displayName;
}
