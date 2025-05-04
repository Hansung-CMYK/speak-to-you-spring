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

    public Ego create(EgoDTO egoDTO) {

        Ego ego = egoDTO.toEntity();

        if(ego.getCreatedAt() == null){
            ego.setCreatedAt(LocalDate.now());
        }

        return egoRepository.save(ego);
    }

    public List<Ego> readAll(){
        return egoRepository.findAll();
    }

    public Ego findById(Long egoId) {
        return egoRepository.findById(egoId)
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));
    }

}
