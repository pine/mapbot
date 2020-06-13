package moe.pine.mapbot.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("bot")
public class BotProperties {
    @NotEmpty String editedMessage;
}
