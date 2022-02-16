package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Agency;
import com.mana.mentor.repository.AgencyRepository;
import com.mana.mentor.service.AgencyService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Agency}.
 */
@Service
@Transactional
public class AgencyServiceImpl implements AgencyService {

    private final Logger log = LoggerFactory.getLogger(AgencyServiceImpl.class);

    private final AgencyRepository agencyRepository;

    public AgencyServiceImpl(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Agency save(Agency agency) {
        log.debug("Request to save Agency : {}", agency);
        return agencyRepository.save(agency);
    }

    @Override
    public Optional<Agency> partialUpdate(Agency agency) {
        log.debug("Request to partially update Agency : {}", agency);

        return agencyRepository
            .findById(agency.getId())
            .map(existingAgency -> {
                if (agency.getTitle() != null) {
                    existingAgency.setTitle(agency.getTitle());
                }
                if (agency.getFirstName() != null) {
                    existingAgency.setFirstName(agency.getFirstName());
                }
                if (agency.getLastName() != null) {
                    existingAgency.setLastName(agency.getLastName());
                }
                if (agency.getIdNumber() != null) {
                    existingAgency.setIdNumber(agency.getIdNumber());
                }
                if (agency.getAddress() != null) {
                    existingAgency.setAddress(agency.getAddress());
                }
                if (agency.getPhone() != null) {
                    existingAgency.setPhone(agency.getPhone());
                }
                if (agency.getEmail() != null) {
                    existingAgency.setEmail(agency.getEmail());
                }
                if (agency.getDateCreated() != null) {
                    existingAgency.setDateCreated(agency.getDateCreated());
                }
                if (agency.getLastModifiedDate() != null) {
                    existingAgency.setLastModifiedDate(agency.getLastModifiedDate());
                }

                return existingAgency;
            })
            .map(agencyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Agency> findAll(Pageable pageable) {
        log.debug("Request to get all Agencies");
        return agencyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agency> findOne(Long id) {
        log.debug("Request to get Agency : {}", id);
        return agencyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agency : {}", id);
        agencyRepository.deleteById(id);
    }
}
