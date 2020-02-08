package moe.pine.mapbot.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("slack")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlackProperties {
    @NotBlank String iconUrl;
    @NotBlank String token;
    @NotBlank String username;
}
