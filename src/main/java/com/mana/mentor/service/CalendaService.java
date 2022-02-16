package com.mana.mentor.service;

import com.mana.mentor.domain.Calenda;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Calenda}.
 */
public interface CalendaService {
    /**
     * Save a calenda.
     *
     * @param calenda the entity to save.
     * @return the persisted entity.
     */
    Calenda save(Calenda calenda);

    /**
     * Partially updates a calenda.
     *
     * @param calenda the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Calenda> partialUpdate(Calenda calenda);

    /**
     * Get all the calendas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Calenda> findAll(Pageable pageable);

    /**
     * Get the "id" calenda.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Calenda> findOne(Long id);

    /**
     * Delete the "id" calenda.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
