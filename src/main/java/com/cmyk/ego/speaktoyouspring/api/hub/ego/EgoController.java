package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ego")
@RequiredArgsConstructor
@Validated
public class EgoController {
    private final EgoService egoService;
    private final EgoApplicationService egoApplicationService;

    /**
     * ego 생성
     * */
    @Operation(summary = "에고를 생성하는 API", description = "ego정보를 생성한다.", tags = {"에고 정보"})
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid EgoDTO egoDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("입력값 오류: " + errorMessage)
                    .build());
        }

        Ego result = egoService.create(egoDTO);
        egoApplicationService.savePersonality(result.getId(), egoDTO.getPersonalityList());

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 생성 완료").data(result).build());
    }

    /**
     * ego테이블에 기록된 전체 ego조회
     */
    @Operation(summary = "모든 에고 정보를 리스트로 조회하는 API", description = "모든 에고 정보를 리스트로 조회한다. 정렬 순서는 ego 테이블의 id 오름차순이다.", tags = {"에고 정보"})
    @GetMapping
    public ResponseEntity readAll() {

        var result = egoService.readAll();

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 조회 완료").data(result).build());
    }

    /**
     * egoid와 일치하는 ego조회
     * */
    @Operation(summary = "egoId로 특정 에고 정보 조회하는 API", description = "egoId를 기준으로 에고 정보를 조회한다. 만약, 성격(=라벨, 태그)를 가지고 있으면, 리스트 형태로 같이 리턴해준다. <br>단, 에고의 성격 정보를 저장하는 테이블과 성격 테이블에 매핑이 이상하면, 성격 정보가 없다는 오류를 출력한다.", tags = {"에고 정보"})
    @GetMapping("/{egoid}")
    public ResponseEntity read(@PathVariable("egoid") Long egoId) {

        var result = egoApplicationService.getEgoInfo(egoId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 조회 완료").data(result).build());
    }

    /**
     * egoid와 일치하는 ego조회
     * */
    @Operation(summary = "egoId로 에고와 연결된 사용자 id를 조회하는 API", description = "egoId로 사용자 Id를 조회한다. 단, 사용자 계정에 같은 에고 id를 추가한 사용자가 있으면(1개의 에고와 2명 이상의 사용자 계정 연결), 데이터베이스 오류를 출력할 수 있다. <br> 오류가 난 경우 /api/v1/user-account/users API를 호출해 확인해볼 것.", tags = {"에고 정보"})
    @GetMapping("/{egoid}/owner")
    public ResponseEntity getOwner(@PathVariable("egoid") Long egoId) {

        var result = egoApplicationService.getUidByEgoId(egoId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("egoId로 uid 조회 완료").data(result).build());
    }

    /**
     * ego정보 수정
     * */
    @Operation(summary = "에고 정보를 갱신하는 API(성격 정보 포함)", description = "egoId를 기준으로 에고 정보를 갱신한다. <br> 성격(=라벨, 태그)의 경우, 해당 성격이 존재하면, 연결해주고, 없으면, 성격을 생성해서 연결해준다. (기존에 에고와 연결된 정보는 전부 삭제되므로, 빈 배열은 수정을 안하는 게 아닌 전부 삭제이다.) 따라서, <b>성격 리스트 내 오타에 주의할 것<b>.", tags = {"에고 정보"})
    @PatchMapping
    public ResponseEntity update(@RequestBody EgoDTO egoDTO) {

        if (egoDTO.getId() == null) {
            return ResponseEntity.badRequest().body(CommonResponse.builder()
                    .code(400)
                    .message("id는 EGO 테이블의 id 값으로 업데이트시 필수 값입니다. (예: \"id\": 8)")
                    .build());
        }

        var result = egoApplicationService.updateEgoInfo(egoDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("ego 수정 완료").data(result).build());
    }

    /**
     * EGO 정보 불러오기
     */
    @Operation(summary = "**평가된** 에고들의 정보를 리스트로 불러오는 API", description = "사용자 ID를 기준으로 에고들의 정보를 불러온다. <br>리스트 조건: <li>생성된 에고이다.</li><li>해당 사용자의 에고 채팅방이 존재해야 한다.</li><li>사용자가 에고를 평가한 적이 있어야 한다.</li>", tags = {"에고 정보"})
    @GetMapping("/{userid}/list")
    public ResponseEntity getUserEgoList(@PathVariable("userid") String userId) {

        List<EgoDTO> result = egoApplicationService.getUserEgoList(userId);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("평가된 에고 목록 조회 완료").data(result).build());
    }

    /** 사용자 ID로 에고 정보 조회 */
    @Operation(summary = "사용자의 사용자 ID로 나의 에고 정보 조회하는 API", description = "userId를 기준으로 에고 정보를 불러온다. <br> userId : user_id_001 <br> 단, 사용자 계정에 같은 에고 id를 추가한 사용자가 있으면(1개의 에고와 2명 이상의 사용자 계정 연결), 데이터베이스 오류를 출력할 수 있다.", tags = {"에고 정보"})
    @GetMapping("/user/{userid}")
    public ResponseEntity getUserEgo(@PathVariable("userid") String userId) {
        Ego result = egoApplicationService.getEgoInfoByUid(userId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message(userId + "의 에고 조회 완료").data(result).build());
    }

    /**
     * 오늘의 에고 불러오기
     */
    @Operation(summary = "특정 사용자의 오늘의 에고를 불러오는 API", description = "오늘의 에고 정보를 불러온다.", tags = {"에고 정보"})
    @GetMapping("/{userId}/daily")
    public ResponseEntity getTodayEgo(@PathVariable("userId") String userId) {
        var result = egoApplicationService.getTodayEgo(userId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("오늘의 에고 정보 조회 완료").data(result).build());
    }

    /**
     * egoId와 userId로 ego 정보 조회
     */
    @Operation(summary = "특정 에고에 대해서 해당 사용자와 연관된 에고의 정보를 모두 조회하는 API(기본, 평가, 성격, 좋아요, 관계)", description = "egoId와 userId를 기준으로 에고 정보를 다양한 테이블에서 전부 불러온다. <br>예외 처리: <li>성격(=라벨, 태그): 에고의 성격이 매핑되어 있는데 성격이 삭제되면 성격이 없다고 오류가 난다. 이때는 에고의 정보를 갱신하면 된다.</li><li>평가: 전체 점수를 리턴하며, 평가한 적이 없으면 null를 리턴한다.</li><li>좋아요: 좋아요(하트 클릭)했으면 true이고, 한 적이 없거나 생성이 안됐으면, false</li><li>에고와 나와의 관계: 매핑이 되어있으면, 해당 문자열을 리턴하고, 없으면, '관계 없음'을 리턴한다.</li>", tags = {"에고 정보"})
    @GetMapping("/{egoId}/{userId}")
    public ResponseEntity getEgoByEgoIdAndUserId(@PathVariable("egoId") Long egoId, @PathVariable("userId") String userId) {
        var result = egoApplicationService.getEgoInfoByUidAndEgoId(userId, egoId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("사용자와 관련된 에고 정보 조회 완료").data(result).build());
    }

    /**
     * 에고 음성 경로 저장하는 API
     */
    @Operation(summary = "에고 음성 경로 저장하는 API", description = "사용자와 관련된 에고 정보를 조회한다. <br>egoId는 필수이다.", tags = {"에고 정보"})
    @PostMapping("/voice/{egoId}")
    public ResponseEntity saveVoicePath(@PathVariable("egoId") Long egoId, @RequestBody String voiceUrl) {
        var result = egoApplicationService.saveVoicePath(egoId, voiceUrl);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("에고 음성 경로 저장 완료").data(result).build());
    }

    /**
     * 에고 음성 경로 조회하는 API
     */
    @Operation(summary = "에고 음성 경로 조회하는 API", description = "사용자와 관련된 에고 정보를 조회한다. <br>egoId는 필수이다.", tags = {"에고 정보"})
    @GetMapping("/voice/{userId}")
    public ResponseEntity getVoicePath(@PathVariable("userId") String userId) {
        var result = egoApplicationService.getVoicePath(userId);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("에고 음성 경로 조회 완료").data(result).build());
    }

    @Operation(summary = "에고 페르소나를 저장하는 API", description = "에고의 세부적인 데이터를 저장한다. <br>모든 정보는 필수이다.", tags = {"에고 정보"})
    @GetMapping("/persona}")
    public ResponseEntity savePersona(@RequestBody PersonaDTO personaDTO) {
        var result = egoApplicationService.savePersona(personaDTO);

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("페르소나 정보 저장 완료").data(result).build());
    }
}
