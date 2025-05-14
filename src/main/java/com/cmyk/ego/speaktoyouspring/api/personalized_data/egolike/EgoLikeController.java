package com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.Ego;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoRepository;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EgoErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ego-like")
@RequiredArgsConstructor
public class EgoLikeController {
    private final EgoLikeService egoLikeService;
    private final UserAccountRepository userAccountRepository;
    private final EgoRepository egoRepository;

    /**
     * ego 좋아요 등록
     */
    @PostMapping("/")
    public ResponseEntity update(@RequestBody EgoLikeDTO egoLikeDTO) {

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(egoLikeDTO.getUid()).orElseThrow(() -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        egoRepository.findById(egoLikeDTO.getEgoId()).orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));

        TenantContext.setCurrentTenant(egoLikeDTO.getUid());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("Update: 좋아요 업데이트된 ego 조회 완료").data(egoLikeService.updateLikes(egoLikeDTO)).build());
    }
}
