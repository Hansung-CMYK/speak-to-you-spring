package com.cmyk.ego.speaktoyouspring.api.tenant.evaluation;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EvaluationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public Evaluation create(EvaluationDTO evaluationDTO){

        int solvingScore = evaluationDTO.getSolvingScore();
        int talkingScore = evaluationDTO.getTalkingScore();
        int overallScore = evaluationDTO.getOverallScore();

        if (solvingScore < 1 || solvingScore > 3 ||
                talkingScore < 1 || talkingScore > 3 ||
                overallScore < 1 || overallScore > 5) {
            throw new ControlledException(EvaluationErrorCode.ERROR_SCORE_OUT_OF_RANGE);
        }

        return evaluationRepository.save(evaluationDTO.toEntity());

    }

}
