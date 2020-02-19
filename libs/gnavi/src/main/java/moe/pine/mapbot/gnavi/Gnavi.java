package moe.pine.mapbot.gnavi;

import moe.pine.mapbot.medium.Place;
import moe.pine.mapbot.medium.Medium;
import java.util.Optional;

/**
 * Gurunavi
 *
 * @see <a href="https://www.gnavi.co.jp/">ぐるなび</a>
 */
public class Gnavi implements Medium {
    @Override
    public Optional<Place> find(String absoluteUrl) {
        return Optional.empty();
    }
}
