package com.mana.mentor.service;

import com.mana.mentor.domain.StudentExams;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link StudentExams}.
 */
public interface StudentExamsService {
    /**
     * Save a studentExams.
     *
     * @param studentExams the entity to save.
     * @return the persisted entity.
     */
    StudentExams save(StudentExams studentExams);

    /**
     * Partially updates a studentExams.
     *
     * @param studentExams the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentExams> partialUpdate(StudentExams studentExams);

    /**
     * Get all the studentExams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentExams> findAll(Pageable pageable);

    /**
     * Get the "id" studentExams.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentExams> findOne(Long id);

    /**
     * Delete the "id" studentExams.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
