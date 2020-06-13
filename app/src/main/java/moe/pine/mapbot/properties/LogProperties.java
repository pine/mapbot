package moe.pine.mapbot.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("log")
public class LogProperties {
    @NotNull Duration retentionPeriod;
}
