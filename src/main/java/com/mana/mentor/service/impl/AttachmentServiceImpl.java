package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Attachment;
import com.mana.mentor.repository.AttachmentRepository;
import com.mana.mentor.service.AttachmentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment save(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Optional<Attachment> partialUpdate(Attachment attachment) {
        log.debug("Request to partially update Attachment : {}", attachment);

        return attachmentRepository
            .findById(attachment.getId())
            .map(existingAttachment -> {
                if (attachment.getName() != null) {
                    existingAttachment.setName(attachment.getName());
                }
                if (attachment.getContentType() != null) {
                    existingAttachment.setContentType(attachment.getContentType());
                }
                if (attachment.getContent() != null) {
                    existingAttachment.setContent(attachment.getContent());
                }
                if (attachment.getContentContentType() != null) {
                    existingAttachment.setContentContentType(attachment.getContentContentType());
                }
                if (attachment.getDateCreated() != null) {
                    existingAttachment.setDateCreated(attachment.getDateCreated());
                }
                if (attachment.getLastModifiedDate() != null) {
                    existingAttachment.setLastModifiedDate(attachment.getLastModifiedDate());
                }

                return existingAttachment;
            })
            .map(attachmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }
}
