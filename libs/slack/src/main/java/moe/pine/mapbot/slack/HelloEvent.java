package moe.pine.mapbot.slack;

import lombok.Value;

@Value
public class HelloEvent implements Event {
    public static final String TYPE = "hello";

    String type;
}
