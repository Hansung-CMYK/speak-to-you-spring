package com.cmyk.ego.speaktoyouspring.api.hub.relationship;


import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/relationship")
@RequiredArgsConstructor
@Validated
public class RelationshipController {
    private final RelationshipService relationshipService;

    /**
     * Relation을 생성하는 API
     */
    @PostMapping("")
    public ResponseEntity create(@RequestBody RelationshipDTO relationshipDTO) {
        relationshipService.create(relationshipDTO);
        var result = "";
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("관계 정보 생성 완료").data(result).build());
    }
}
