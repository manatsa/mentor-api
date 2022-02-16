package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExerciseAnswer;
import com.mana.mentor.repository.ExerciseAnswerRepository;
import com.mana.mentor.service.ExerciseAnswerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseAnswer}.
 */
@Service
@Transactional
public class ExerciseAnswerServiceImpl implements ExerciseAnswerService {

    private final Logger log = LoggerFactory.getLogger(ExerciseAnswerServiceImpl.class);

    private final ExerciseAnswerRepository exerciseAnswerRepository;

    public ExerciseAnswerServiceImpl(ExerciseAnswerRepository exerciseAnswerRepository) {
        this.exerciseAnswerRepository = exerciseAnswerRepository;
    }

    @Override
    public ExerciseAnswer save(ExerciseAnswer exerciseAnswer) {
        log.debug("Request to save ExerciseAnswer : {}", exerciseAnswer);
        return exerciseAnswerRepository.save(exerciseAnswer);
    }

    @Override
    public Optional<ExerciseAnswer> partialUpdate(ExerciseAnswer exerciseAnswer) {
        log.debug("Request to partially update ExerciseAnswer : {}", exerciseAnswer);

        return exerciseAnswerRepository
            .findById(exerciseAnswer.getId())
            .map(existingExerciseAnswer -> {
                if (exerciseAnswer.getAnswer() != null) {
                    existingExerciseAnswer.setAnswer(exerciseAnswer.getAnswer());
                }
                if (exerciseAnswer.getExplanation() != null) {
                    existingExerciseAnswer.setExplanation(exerciseAnswer.getExplanation());
                }
                if (exerciseAnswer.getDateCreated() != null) {
                    existingExerciseAnswer.setDateCreated(exerciseAnswer.getDateCreated());
                }
                if (exerciseAnswer.getLastModifiedDate() != null) {
                    existingExerciseAnswer.setLastModifiedDate(exerciseAnswer.getLastModifiedDate());
                }

                return existingExerciseAnswer;
            })
            .map(exerciseAnswerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseAnswer> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseAnswers");
        return exerciseAnswerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseAnswer> findOne(Long id) {
        log.debug("Request to get ExerciseAnswer : {}", id);
        return exerciseAnswerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseAnswer : {}", id);
        exerciseAnswerRepository.deleteById(id);
    }
}
