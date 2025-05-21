package com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByEgoId(Long egoId);
}
