# mapbot &nbsp;[![CircleCI](https://circleci.com/gh/pine/mapbot/tree/master.svg?style=shield)](https://circleci.com/gh/pine/mapbot/tree/master) [![codecov](https://codecov.io/gh/pine/mapbot/branch/master/graph/badge.svg)](https://codecov.io/gh/pine/mapbot)
:world_map: Google Maps bot

## Requirements
- Java 11 or later
- Redis

## Libraries
- Spring Boot 2.x

### Deployment

```sh
$ heroku apps:create your-app
$ heroku config:set SPRING_PROFILES_ACTIVE=prod
$ heroku config:set TZ=Asia/Tokyo
$ heroku config:set 'JAVA_OPTS=-Xmx200m -XX:+UseCompressedOops -XX:+UseStringDeduplication --illegal-access=deny'

# Setup Redis
$ heroku addons:create heroku-redis:hobby-dev

# Deploy JAR file
$ ./gradlew build
$ heroku plugins:install java
$ heroku deploy:jar --jar app/build/libs/app.jar --jdk 11
```

## License
MIT &copy; Pine Mizune
