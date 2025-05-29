package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EgoRepository extends JpaRepository<Ego, Long> {
    Optional<Ego> findById(Long egoId);
}
