package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EgoRelationshipRepository extends JpaRepository<EgoRelationship, Long> {
    List<EgoRelationship> findByUid(String userId);

    Optional<EgoRelationship> findByUidAndEgoId(String userId, Long egoId);
}
