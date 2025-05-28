package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;

import com.cmyk.ego.speaktoyouspring.api.hub.relationship.RelationshipService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EgoRelationshipErrorCode;
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
        List<EgoRelationship> egoRelationshipList = egoRelationshipRepository.findByUid(userId);
        if(egoRelationshipList.isEmpty()){
            throw new ControlledException(EgoRelationshipErrorCode.ERROR_EGO_RELATIONSHIP_NOT_FOUND);
        }
        for(EgoRelationship egoRelationship : egoRelationshipList){
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
        EgoRelationship egoRelationship = egoRelationshipRepository.findByUidAndEgoId(uid, egoId).orElseGet(
                () -> new EgoRelationship(null, uid, egoId, null, null));

        return relationshipService.findByRelationshipId(egoRelationship.getRelationshipId()).getRelationshipContent();
    }

    // 사용자 ID로 에고 관계 리스트 전체 조회
    public List<EgoRelationship> readAll(String uid) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        return egoRelationshipRepository.findByUid(uid);
    }

    public EgoRelationship read(String uid, Long egoRelationshipId) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        return egoRelationshipRepository.findByUidAndEgoRelationshipId(uid, egoRelationshipId).orElseThrow(
                () -> new ControlledException(EgoRelationshipErrorCode.ERROR_EGO_RELATIONSHIP_NOT_FOUND));
    }

    /// 사용자 id로 에고 관계 전체 삭제
    public void deleteAll(String uid) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        egoRelationshipRepository.deleteAll();
    }

    /// 사용자 id와 에고 관계 id로 특정 에고 관계 삭제
    public void delete(String uid, Long egoRelationshipId) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        egoRelationshipRepository.findByEgoRelationshipId(egoRelationshipId).orElseThrow(
                () -> new ControlledException(EgoRelationshipErrorCode.ERROR_EGO_RELATIONSHIP_NOT_FOUND)
        );

        egoRelationshipRepository.deleteByEgoRelationshipId(egoRelationshipId);

    }
}
