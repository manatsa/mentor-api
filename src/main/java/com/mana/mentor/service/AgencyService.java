package com.mana.mentor.service;

import com.mana.mentor.domain.Agency;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Agency}.
 */
public interface AgencyService {
    /**
     * Save a agency.
     *
     * @param agency the entity to save.
     * @return the persisted entity.
     */
    Agency save(Agency agency);

    /**
     * Partially updates a agency.
     *
     * @param agency the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Agency> partialUpdate(Agency agency);

    /**
     * Get all the agencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Agency> findAll(Pageable pageable);

    /**
     * Get the "id" agency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Agency> findOne(Long id);

    /**
     * Delete the "id" agency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
