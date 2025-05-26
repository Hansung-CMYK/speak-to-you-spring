package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import com.cmyk.ego.speaktoyouspring.api.hub.relationship.RelationshipService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EgoRelationshipService {
    private final RelationshipService relationshipService;
    private final EgoRelationshipRepository egoRelationshipRepository;
    private final UserAccountRepository userAccountRepository;

    ///  사용자 ID 로 에고 관계 리스트 가져오기
    public List<EgoRelationshipResponseDTO> findByUserId(String userId){
        userAccountRepository.findByUid(userId).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(userId);

        List<EgoRelationshipResponseDTO> erList = new ArrayList<>();
        for(EgoRelationship egoRelationship : egoRelationshipRepository.findByUid(userId)){
            EgoRelationshipResponseDTO er = new EgoRelationshipResponseDTO(
                    egoRelationship.getEgoRelationshipId(),
                    egoRelationship.getUid(),
                    egoRelationship.getEgoId(),
                    egoRelationship.getRelationshipId(),
                    egoRelationship.getCreatedAt(),
                    relationshipService.findByRelationshipId(egoRelationship.getRelationshipId()).getRelationshipContent()
            );
            erList.add(er);
        }

        return erList;
    }

    @Transactional
    public EgoRelationship create(EgoRelationshipDTO egoRelationshipDTO){
        String uid = egoRelationshipDTO.getUid();
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);
        egoRelationshipDTO.setCreatedAt(LocalDateTime.now());

        // upsert
        EgoRelationship er = egoRelationshipRepository.findByUidAndEgoId(uid, egoRelationshipDTO.getEgoId()).orElseGet(
                egoRelationshipDTO::toEntity
        );

        er.setRelationshipId(egoRelationshipDTO.getRelationshipId());

        return egoRelationshipRepository.save(er);
    }

    public String findEgoRelationshipIdByEgoIdAndUid(String uid, Long egoId){
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        EgoRelationship egoRelationship = egoRelationshipRepository.findByUidAndEgoId(uid, egoId).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        return relationshipService.findByRelationshipId(egoRelationship.getRelationshipId()).getRelationshipContent();
    }
}
