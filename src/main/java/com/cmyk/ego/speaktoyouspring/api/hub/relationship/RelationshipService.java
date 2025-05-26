package com.cmyk.ego.speaktoyouspring.api.hub.relationship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;

    /// 생성
    public void create(RelationshipDTO relationshipDTO) {
        Relationship relationship = relationshipRepository.findByRelationshipContent(relationshipDTO.getRelationshipContent())
                .orElseGet(relationshipDTO::toEntity);
        relationshipRepository.save(relationship);
    }

    /// 조회
    public Relationship findByRelationshipId(Long relationshipId) {
        return relationshipRepository.findByRelationshipId(relationshipId).orElse(new Relationship(relationshipId, "관계 없음"));
    }

}
