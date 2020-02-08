package moe.pine.mapbot;

import moe.pine.heroku.addons.HerokuRedis;

public final class AppInitializer {
    public static void run() {
        HerokuRedis redis = HerokuRedis.get();
        if (redis != null) {
            System.setProperty("spring.redis.host", redis.getHost());
            System.setProperty("spring.redis.password", redis.getPassword());
            System.setProperty("spring.redis.port", Integer.toString(redis.getPort()));
        }
    }
}
