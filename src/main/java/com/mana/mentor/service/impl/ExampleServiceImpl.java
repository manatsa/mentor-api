package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Example;
import com.mana.mentor.repository.ExampleRepository;
import com.mana.mentor.service.ExampleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Example}.
 */
@Service
@Transactional
public class ExampleServiceImpl implements ExampleService {

    private final Logger log = LoggerFactory.getLogger(ExampleServiceImpl.class);

    private final ExampleRepository exampleRepository;

    public ExampleServiceImpl(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public Example save(Example example) {
        log.debug("Request to save Example : {}", example);
        return exampleRepository.save(example);
    }

    @Override
    public Optional<Example> partialUpdate(Example example) {
        log.debug("Request to partially update Example : {}", example);

        return exampleRepository
            .findById(example.getId())
            .map(existingExample -> {
                if (example.getName() != null) {
                    existingExample.setName(example.getName());
                }
                if (example.getContent() != null) {
                    existingExample.setContent(example.getContent());
                }
                if (example.getPicture() != null) {
                    existingExample.setPicture(example.getPicture());
                }
                if (example.getPictureContentType() != null) {
                    existingExample.setPictureContentType(example.getPictureContentType());
                }
                if (example.getDateCreated() != null) {
                    existingExample.setDateCreated(example.getDateCreated());
                }
                if (example.getLastModifiedDate() != null) {
                    existingExample.setLastModifiedDate(example.getLastModifiedDate());
                }

                return existingExample;
            })
            .map(exampleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Example> findAll(Pageable pageable) {
        log.debug("Request to get all Examples");
        return exampleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Example> findOne(Long id) {
        log.debug("Request to get Example : {}", id);
        return exampleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Example : {}", id);
        exampleRepository.deleteById(id);
    }
}
