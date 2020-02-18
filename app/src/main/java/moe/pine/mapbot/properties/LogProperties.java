package moe.pine.mapbot.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties("log")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogProperties {
    @NotNull Duration retentionPeriod;
}
