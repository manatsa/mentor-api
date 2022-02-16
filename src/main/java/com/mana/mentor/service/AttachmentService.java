package com.mana.mentor.service;

import com.mana.mentor.domain.Attachment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Attachment}.
 */
public interface AttachmentService {
    /**
     * Save a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    Attachment save(Attachment attachment);

    /**
     * Partially updates a attachment.
     *
     * @param attachment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Attachment> partialUpdate(Attachment attachment);

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Attachment> findAll(Pageable pageable);

    /**
     * Get the "id" attachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Attachment> findOne(Long id);

    /**
     * Delete the "id" attachment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
