package moe.pine.mapbot.medium;

import java.util.Optional;

public interface Medium {
    Optional<Place> find(String absoluteUrl);
}
