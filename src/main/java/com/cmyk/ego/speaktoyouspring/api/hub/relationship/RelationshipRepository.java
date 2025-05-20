package com.cmyk.ego.speaktoyouspring.api.hub.relationship;

import com.cmyk.ego.speaktoyouspring.api.hub.personality.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Optional<Relationship> findByRelationshipContent(String relationshipContent);

    Optional<Relationship> findByRelationshipId(Long relationshipId);
}
