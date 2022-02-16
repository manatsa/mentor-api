package com.mana.mentor.service;

import com.mana.mentor.domain.Exam;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Exam}.
 */
public interface ExamService {
    /**
     * Save a exam.
     *
     * @param exam the entity to save.
     * @return the persisted entity.
     */
    Exam save(Exam exam);

    /**
     * Partially updates a exam.
     *
     * @param exam the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Exam> partialUpdate(Exam exam);

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Exam> findAll(Pageable pageable);

    /**
     * Get the "id" exam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Exam> findOne(Long id);

    /**
     * Delete the "id" exam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
