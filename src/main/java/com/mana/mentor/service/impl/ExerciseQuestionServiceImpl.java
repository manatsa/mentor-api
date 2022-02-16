package com.mana.mentor.service.impl;

import com.mana.mentor.domain.ExerciseQuestion;
import com.mana.mentor.repository.ExerciseQuestionRepository;
import com.mana.mentor.service.ExerciseQuestionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExerciseQuestion}.
 */
@Service
@Transactional
public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

    private final Logger log = LoggerFactory.getLogger(ExerciseQuestionServiceImpl.class);

    private final ExerciseQuestionRepository exerciseQuestionRepository;

    public ExerciseQuestionServiceImpl(ExerciseQuestionRepository exerciseQuestionRepository) {
        this.exerciseQuestionRepository = exerciseQuestionRepository;
    }

    @Override
    public ExerciseQuestion save(ExerciseQuestion exerciseQuestion) {
        log.debug("Request to save ExerciseQuestion : {}", exerciseQuestion);
        return exerciseQuestionRepository.save(exerciseQuestion);
    }

    @Override
    public Optional<ExerciseQuestion> partialUpdate(ExerciseQuestion exerciseQuestion) {
        log.debug("Request to partially update ExerciseQuestion : {}", exerciseQuestion);

        return exerciseQuestionRepository
            .findById(exerciseQuestion.getId())
            .map(existingExerciseQuestion -> {
                if (exerciseQuestion.getName() != null) {
                    existingExerciseQuestion.setName(exerciseQuestion.getName());
                }
                if (exerciseQuestion.getQuestion() != null) {
                    existingExerciseQuestion.setQuestion(exerciseQuestion.getQuestion());
                }
                if (exerciseQuestion.getAnswer() != null) {
                    existingExerciseQuestion.setAnswer(exerciseQuestion.getAnswer());
                }
                if (exerciseQuestion.getExplanation() != null) {
                    existingExerciseQuestion.setExplanation(exerciseQuestion.getExplanation());
                }
                if (exerciseQuestion.getDateCreated() != null) {
                    existingExerciseQuestion.setDateCreated(exerciseQuestion.getDateCreated());
                }
                if (exerciseQuestion.getLastModifiedDate() != null) {
                    existingExerciseQuestion.setLastModifiedDate(exerciseQuestion.getLastModifiedDate());
                }

                return existingExerciseQuestion;
            })
            .map(exerciseQuestionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseQuestion> findAll(Pageable pageable) {
        log.debug("Request to get all ExerciseQuestions");
        return exerciseQuestionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseQuestion> findOne(Long id) {
        log.debug("Request to get ExerciseQuestion : {}", id);
        return exerciseQuestionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseQuestion : {}", id);
        exerciseQuestionRepository.deleteById(id);
    }
}
