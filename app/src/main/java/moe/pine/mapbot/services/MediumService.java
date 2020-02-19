package moe.pine.mapbot.services;

import moe.pine.mapbot.medium.Medium;
import moe.pine.mapbot.medium.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediumService {
    private final List<Medium> mediums;

    public MediumService(Medium... mediums) {
        this.mediums = List.of(mediums);
    }

    public Optional<Place> find(String absoluteUrl) {
        return mediums.stream()
                .flatMap(medium -> medium.find(absoluteUrl).stream())
                .findFirst();
    }
}
