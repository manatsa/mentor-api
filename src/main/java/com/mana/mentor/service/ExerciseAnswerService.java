package com.mana.mentor.service;

import com.mana.mentor.domain.ExerciseAnswer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseAnswer}.
 */
public interface ExerciseAnswerService {
    /**
     * Save a exerciseAnswer.
     *
     * @param exerciseAnswer the entity to save.
     * @return the persisted entity.
     */
    ExerciseAnswer save(ExerciseAnswer exerciseAnswer);

    /**
     * Partially updates a exerciseAnswer.
     *
     * @param exerciseAnswer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseAnswer> partialUpdate(ExerciseAnswer exerciseAnswer);

    /**
     * Get all the exerciseAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseAnswer> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseAnswer> findOne(Long id);

    /**
     * Delete the "id" exerciseAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
