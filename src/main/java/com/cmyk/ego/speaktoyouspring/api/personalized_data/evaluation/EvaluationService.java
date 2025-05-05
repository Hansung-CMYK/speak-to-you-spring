package com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EvaluationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public Evaluation create(EvaluationDTO evaluationDTO){

        int solvingScore = evaluationDTO.getSolvingScore();
        int talkingScore = evaluationDTO.getTalkingScore();
        int overallScore = evaluationDTO.getOverallScore();

        if (solvingScore < EvaluationScoreRange.MIN_SCORE || solvingScore > EvaluationScoreRange.MAX_SCORE_THREE ||
                talkingScore < EvaluationScoreRange.MIN_SCORE || talkingScore > EvaluationScoreRange.MAX_SCORE_THREE ||
                overallScore < EvaluationScoreRange.MIN_SCORE || overallScore > EvaluationScoreRange.MAX_SCORE_FIVE) {
            throw new ControlledException(EvaluationErrorCode.ERROR_SCORE_OUT_OF_RANGE);
        }

        return evaluationRepository.save(evaluationDTO.toEntity());
    }

    public List<Evaluation> readAll(){
        return evaluationRepository.findAll();
    }

}
