package com.mana.mentor.service;

import com.mana.mentor.domain.Guardian;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Guardian}.
 */
public interface GuardianService {
    /**
     * Save a guardian.
     *
     * @param guardian the entity to save.
     * @return the persisted entity.
     */
    Guardian save(Guardian guardian);

    /**
     * Partially updates a guardian.
     *
     * @param guardian the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Guardian> partialUpdate(Guardian guardian);

    /**
     * Get all the guardians.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Guardian> findAll(Pageable pageable);

    /**
     * Get the "id" guardian.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Guardian> findOne(Long id);

    /**
     * Delete the "id" guardian.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
