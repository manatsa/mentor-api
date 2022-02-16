package com.mana.mentor.service;

import com.mana.mentor.domain.School;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link School}.
 */
public interface SchoolService {
    /**
     * Save a school.
     *
     * @param school the entity to save.
     * @return the persisted entity.
     */
    School save(School school);

    /**
     * Partially updates a school.
     *
     * @param school the entity to update partially.
     * @return the persisted entity.
     */
    Optional<School> partialUpdate(School school);

    /**
     * Get all the schools.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<School> findAll(Pageable pageable);

    /**
     * Get the "id" school.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<School> findOne(Long id);

    /**
     * Delete the "id" school.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
