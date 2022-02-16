package com.mana.mentor.service;

import com.mana.mentor.domain.ExerciseQuestion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseQuestion}.
 */
public interface ExerciseQuestionService {
    /**
     * Save a exerciseQuestion.
     *
     * @param exerciseQuestion the entity to save.
     * @return the persisted entity.
     */
    ExerciseQuestion save(ExerciseQuestion exerciseQuestion);

    /**
     * Partially updates a exerciseQuestion.
     *
     * @param exerciseQuestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseQuestion> partialUpdate(ExerciseQuestion exerciseQuestion);

    /**
     * Get all the exerciseQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseQuestion> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseQuestion> findOne(Long id);

    /**
     * Delete the "id" exerciseQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
