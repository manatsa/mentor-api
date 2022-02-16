package com.mana.mentor.service;

import com.mana.mentor.domain.ExamAnswer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExamAnswer}.
 */
public interface ExamAnswerService {
    /**
     * Save a examAnswer.
     *
     * @param examAnswer the entity to save.
     * @return the persisted entity.
     */
    ExamAnswer save(ExamAnswer examAnswer);

    /**
     * Partially updates a examAnswer.
     *
     * @param examAnswer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExamAnswer> partialUpdate(ExamAnswer examAnswer);

    /**
     * Get all the examAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamAnswer> findAll(Pageable pageable);

    /**
     * Get the "id" examAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamAnswer> findOne(Long id);

    /**
     * Delete the "id" examAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
