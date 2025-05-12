package com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EgoLikeRepository extends JpaRepository<EgoLike, Long> {
    Optional<EgoLike> findByEgoIdAndUid(Long egoId, String uid);
}
