package moe.pine.mapbot.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
@ConfigurationProperties("bot")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotProperties {
    @NotEmpty String editedMessage;
}
