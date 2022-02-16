package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Guardian;
import com.mana.mentor.repository.GuardianRepository;
import com.mana.mentor.service.GuardianService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Guardian}.
 */
@Service
@Transactional
public class GuardianServiceImpl implements GuardianService {

    private final Logger log = LoggerFactory.getLogger(GuardianServiceImpl.class);

    private final GuardianRepository guardianRepository;

    public GuardianServiceImpl(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    @Override
    public Guardian save(Guardian guardian) {
        log.debug("Request to save Guardian : {}", guardian);
        return guardianRepository.save(guardian);
    }

    @Override
    public Optional<Guardian> partialUpdate(Guardian guardian) {
        log.debug("Request to partially update Guardian : {}", guardian);

        return guardianRepository
            .findById(guardian.getId())
            .map(existingGuardian -> {
                if (guardian.getTitle() != null) {
                    existingGuardian.setTitle(guardian.getTitle());
                }
                if (guardian.getFirstName() != null) {
                    existingGuardian.setFirstName(guardian.getFirstName());
                }
                if (guardian.getLastName() != null) {
                    existingGuardian.setLastName(guardian.getLastName());
                }
                if (guardian.getIdNumber() != null) {
                    existingGuardian.setIdNumber(guardian.getIdNumber());
                }
                if (guardian.getAddress() != null) {
                    existingGuardian.setAddress(guardian.getAddress());
                }
                if (guardian.getPhone() != null) {
                    existingGuardian.setPhone(guardian.getPhone());
                }
                if (guardian.getEmail() != null) {
                    existingGuardian.setEmail(guardian.getEmail());
                }
                if (guardian.getDateCreated() != null) {
                    existingGuardian.setDateCreated(guardian.getDateCreated());
                }
                if (guardian.getLastModifiedDate() != null) {
                    existingGuardian.setLastModifiedDate(guardian.getLastModifiedDate());
                }

                return existingGuardian;
            })
            .map(guardianRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Guardian> findAll(Pageable pageable) {
        log.debug("Request to get all Guardians");
        return guardianRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guardian> findOne(Long id) {
        log.debug("Request to get Guardian : {}", id);
        return guardianRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guardian : {}", id);
        guardianRepository.deleteById(id);
    }
}
