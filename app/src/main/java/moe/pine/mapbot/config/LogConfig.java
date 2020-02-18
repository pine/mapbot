package moe.pine.mapbot.config;

import moe.pine.mapbot.log.SentLogRepository;
import moe.pine.mapbot.properties.LogProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogConfig {
    @Bean
    public SentLogRepository sentLogRepository(
            ReactiveStringRedisTemplate redisTemplate,
            LogProperties logProperties
    ) {
        return new SentLogRepository(
                redisTemplate,
                logProperties.getRetentionPeriod()
        );
    }
}
