package moe.pine.mapbot.config;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Value
@NonFinal
@ConstructorBinding
@ConfigurationProperties("medium")
public class MediumProperties {
    @NotNull Set<String> deniedHosts;
}
