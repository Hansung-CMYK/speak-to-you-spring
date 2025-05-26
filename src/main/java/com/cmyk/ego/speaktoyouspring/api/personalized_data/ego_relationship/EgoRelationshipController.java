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
    @Operation(summary = "사용자의 ego 관계 리스트 조회 API", description = "사용자의 ego 관계 리스트를 조회하는 API")
    @GetMapping("/{userId}")
    public ResponseEntity get(@PathVariable String userId) {
        List<EgoRelationshipResponseDTO> result = egoRelationshipService.findByUserId(userId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 리스트 조회 완료").data(result).build());
    }

    // 에고 관계 생성
    @Operation(summary = "사용자의 ego 관계 수성/생성 API", description = "사용자의 ego 관계를 생성/수정하는 API 에고ID와 사용자ID로 조회하여 존재하면, relationId를 갱신하고 다르면 생성한다.")
    @PostMapping
    public ResponseEntity create(@RequestBody EgoRelationshipDTO egoRelationshipDTO) {
        egoRelationshipService.create(egoRelationshipDTO);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 생성 완료").build());
    }

    // 에고 관계 단일 조회
    @GetMapping("/{userId}/{egoRelationId}")
    public ResponseEntity egoRelationRead(@PathVariable String userId, @PathVariable Long egoRelationId) {
        EgoRelationship result = egoRelationshipService.read(userId, egoRelationId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message(userId + "사용자의 ego 관계 단일 조회 완료").data(result).build());
    }

    // 에고 관계 전체 삭제 API
    @Operation(summary = "사용자의 ego 관계 전체 삭제 API", description = "[관리자]: 특정 사용자의 ego 관계를 모두 삭제하는 API")
    @DeleteMapping("/{userId}")
    public ResponseEntity egoRelationDelete(@PathVariable String userId) {
        egoRelationshipService.deleteAll(userId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 삭제 완료").build());
    }

    // 에고 관계 단일 삭제 API
    @Operation(summary = "사용자의 ego 관계 단일 삭제 API", description = "[관리자]: 특정 사용자의 ego 관계를 단일 삭제하는 API")
    @DeleteMapping("/{userId}/{egoRelationId}")
    public ResponseEntity egoRelationDelete(@PathVariable String userId, @PathVariable Long egoRelationId) {
        egoRelationshipService.delete(userId, egoRelationId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자의 ego 관계 단일 삭제 완료").build());
    }

}
