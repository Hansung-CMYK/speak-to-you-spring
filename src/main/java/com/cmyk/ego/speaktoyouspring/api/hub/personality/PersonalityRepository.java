package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {
    Optional<Personality> findByPersonalityId(Long personalityId);

    Optional<Personality> findByContent(String content);
}
