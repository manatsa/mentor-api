package com.mana.mentor.service;

import com.mana.mentor.domain.ExamMark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExamMark}.
 */
public interface ExamMarkService {
    /**
     * Save a examMark.
     *
     * @param examMark the entity to save.
     * @return the persisted entity.
     */
    ExamMark save(ExamMark examMark);

    /**
     * Partially updates a examMark.
     *
     * @param examMark the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExamMark> partialUpdate(ExamMark examMark);

    /**
     * Get all the examMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamMark> findAll(Pageable pageable);

    /**
     * Get the "id" examMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamMark> findOne(Long id);

    /**
     * Delete the "id" examMark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
