package com.mana.mentor.service;

import com.mana.mentor.domain.Example;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Example}.
 */
public interface ExampleService {
    /**
     * Save a example.
     *
     * @param example the entity to save.
     * @return the persisted entity.
     */
    Example save(Example example);

    /**
     * Partially updates a example.
     *
     * @param example the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Example> partialUpdate(Example example);

    /**
     * Get all the examples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Example> findAll(Pageable pageable);

    /**
     * Get the "id" example.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Example> findOne(Long id);

    /**
     * Delete the "id" example.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
