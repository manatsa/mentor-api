package com.mana.mentor.service;

import com.mana.mentor.domain.ExamQuestion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ExamQuestion}.
 */
public interface ExamQuestionService {
    /**
     * Save a examQuestion.
     *
     * @param examQuestion the entity to save.
     * @return the persisted entity.
     */
    ExamQuestion save(ExamQuestion examQuestion);

    /**
     * Partially updates a examQuestion.
     *
     * @param examQuestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExamQuestion> partialUpdate(ExamQuestion examQuestion);

    /**
     * Get all the examQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamQuestion> findAll(Pageable pageable);

    /**
     * Get the "id" examQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamQuestion> findOne(Long id);

    /**
     * Delete the "id" examQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
