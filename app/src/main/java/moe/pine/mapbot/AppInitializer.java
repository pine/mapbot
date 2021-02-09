package moe.pine.mapbot;

import lombok.extern.slf4j.Slf4j;
import moe.pine.heroku.addons.HerokuRedis;

@Slf4j
public final class AppInitializer {
    public static void run() {
        String redisUrl = System.getenv("REDIS_URL");
        log.info("REDIS_URL: {}", redisUrl);

        HerokuRedis redis = HerokuRedis.get();
        if (redis != null) {
            log.info("HerokuRedis: {}", redis);

            System.setProperty("spring.redis.host", redis.getHost());
            System.setProperty("spring.redis.password", redis.getPassword());
            System.setProperty("spring.redis.port", Integer.toString(redis.getPort()));
        }
    }
}
