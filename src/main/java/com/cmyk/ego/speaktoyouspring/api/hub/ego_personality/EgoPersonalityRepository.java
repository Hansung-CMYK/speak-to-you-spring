package com.cmyk.ego.speaktoyouspring.api.hub.ego_personality;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EgoPersonalityRepository extends JpaRepository<EgoPersonality, Long> {
    List<EgoPersonality> findByEgoId(Long egoId);

    Optional<EgoPersonality> findByEgoIdAndPersonalityId(Long egoId, Long personalityId);

    void deleteAllByEgoId(Long egoId);
}
