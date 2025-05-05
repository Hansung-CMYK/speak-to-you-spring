package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EgoErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EgoService {
    private final EgoRepository egoRepository;

    // ego 생성
    public Ego create(EgoDTO egoDTO) {

        Ego ego = egoDTO.toEntity();

        if (ego.getCreatedAt() == null) {
            ego.setCreatedAt(LocalDate.now());
        }

        return egoRepository.save(ego);
    }

    // ego 전체 조회
    public List<Ego> readAll() {
        return egoRepository.findAll();
    }

    // egoid와 일치하는 ego 조회
    public Ego findById(Long egoId) {
        return egoRepository.findById(egoId)
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));
    }

    // ego 정보 수정
    public Ego update(EgoDTO egoDTO) {
        Ego ego = egoRepository.findById(egoDTO.getId())
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));

        if (egoDTO.getName() != null)
            ego.setName(egoDTO.getName());
        if (egoDTO.getIntroduction() != null)
            ego.setIntroduction(egoDTO.getIntroduction());
        if (egoDTO.getProfileImage() != null)
            ego.setProfileImage(egoDTO.getProfileImage());
        if (egoDTO.getMbti() != null)
            ego.setMbti(egoDTO.getMbti());
        if (egoDTO.getPersonality() != null)
            ego.setPersonality(egoDTO.getPersonality());
        if (egoDTO.getCreatedAt() != null)
            ego.setCreatedAt(egoDTO.getCreatedAt());

        return ego;
    }

}
