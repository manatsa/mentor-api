package com.mana.mentor.service;

import com.mana.mentor.domain.ExerciseMark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExerciseMark}.
 */
public interface ExerciseMarkService {
    /**
     * Save a exerciseMark.
     *
     * @param exerciseMark the entity to save.
     * @return the persisted entity.
     */
    ExerciseMark save(ExerciseMark exerciseMark);

    /**
     * Partially updates a exerciseMark.
     *
     * @param exerciseMark the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExerciseMark> partialUpdate(ExerciseMark exerciseMark);

    /**
     * Get all the exerciseMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExerciseMark> findAll(Pageable pageable);

    /**
     * Get the "id" exerciseMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseMark> findOne(Long id);

    /**
     * Delete the "id" exerciseMark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
