package com.mana.mentor.service;

import com.mana.mentor.domain.Lecture;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Lecture}.
 */
public interface LectureService {
    /**
     * Save a lecture.
     *
     * @param lecture the entity to save.
     * @return the persisted entity.
     */
    Lecture save(Lecture lecture);

    /**
     * Partially updates a lecture.
     *
     * @param lecture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Lecture> partialUpdate(Lecture lecture);

    /**
     * Get all the lectures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lecture> findAll(Pageable pageable);

    /**
     * Get the "id" lecture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Lecture> findOne(Long id);

    /**
     * Delete the "id" lecture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
