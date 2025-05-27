package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EgoRelationshipRepository extends JpaRepository<EgoRelationship, Long> {
    List<EgoRelationship> findByUid(String userId);

    @Transactional
    Optional<EgoRelationship> findByUidAndEgoId(String userId, Long egoId);

    Optional<EgoRelationship> findByUidAndEgoRelationshipId(String userId, Long egoRelationshipId);

    @Modifying
    @Transactional
    @Query("delete from EgoRelationship e where e.egoRelationshipId = :egoRelationshipId")
    void deleteByEgoRelationshipId(Long egoRelationshipId);

    Optional<EgoRelationship> findByEgoRelationshipId(Long egoRelationshipId);
}
