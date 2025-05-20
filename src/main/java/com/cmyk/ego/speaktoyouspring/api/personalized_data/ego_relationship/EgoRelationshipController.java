package com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship;


import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ego-relationship")
@RequiredArgsConstructor
@Validated
public class EgoRelationshipController {
    private final EgoRelationshipService egoRelationshipService;
    
    // 사용자 id로 에고 관계 리스트 가져오기
    @Operation(summary = "사용자의 ego 관계 조회 API", description = "사용자의 ego 관계 리스트를 조회하는 API")
    @GetMapping("/{userId}")
    public ResponseEntity findByUserId(@PathVariable String userId) {
        List<EgoRelationshipResponseDTO> result = egoRelationshipService.findByUserId(userId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 조회 완료").data(result).build());
    }

    // 에고 관계 생성
    @Operation(summary = "사용자의 ego 관계 생성 API", description = "사용자의 ego 관계를 생성하는 API")
    @PostMapping
    public ResponseEntity create(@RequestBody EgoRelationshipDTO egoRelationshipDTO) {
        egoRelationshipService.create(egoRelationshipDTO);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 생성 완료").build());
    }
}
