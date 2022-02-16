package com.mana.mentor.service;

import com.mana.mentor.domain.News;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link News}.
 */
public interface NewsService {
    /**
     * Save a news.
     *
     * @param news the entity to save.
     * @return the persisted entity.
     */
    News save(News news);

    /**
     * Partially updates a news.
     *
     * @param news the entity to update partially.
     * @return the persisted entity.
     */
    Optional<News> partialUpdate(News news);

    /**
     * Get all the news.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<News> findAll(Pageable pageable);

    /**
     * Get the "id" news.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<News> findOne(Long id);

    /**
     * Delete the "id" news.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
