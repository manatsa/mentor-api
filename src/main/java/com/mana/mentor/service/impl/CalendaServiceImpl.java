package com.mana.mentor.service.impl;

import com.mana.mentor.domain.Calenda;
import com.mana.mentor.repository.CalendaRepository;
import com.mana.mentor.service.CalendaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Calenda}.
 */
@Service
@Transactional
public class CalendaServiceImpl implements CalendaService {

    private final Logger log = LoggerFactory.getLogger(CalendaServiceImpl.class);

    private final CalendaRepository calendaRepository;

    public CalendaServiceImpl(CalendaRepository calendaRepository) {
        this.calendaRepository = calendaRepository;
    }

    @Override
    public Calenda save(Calenda calenda) {
        log.debug("Request to save Calenda : {}", calenda);
        return calendaRepository.save(calenda);
    }

    @Override
    public Optional<Calenda> partialUpdate(Calenda calenda) {
        log.debug("Request to partially update Calenda : {}", calenda);

        return calendaRepository
            .findById(calenda.getId())
            .map(existingCalenda -> {
                if (calenda.getEventName() != null) {
                    existingCalenda.setEventName(calenda.getEventName());
                }
                if (calenda.getDescription() != null) {
                    existingCalenda.setDescription(calenda.getDescription());
                }
                if (calenda.getCategory() != null) {
                    existingCalenda.setCategory(calenda.getCategory());
                }
                if (calenda.getEventStartDate() != null) {
                    existingCalenda.setEventStartDate(calenda.getEventStartDate());
                }
                if (calenda.getEventEndDate() != null) {
                    existingCalenda.setEventEndDate(calenda.getEventEndDate());
                }
                if (calenda.getLocation() != null) {
                    existingCalenda.setLocation(calenda.getLocation());
                }
                if (calenda.getDateCreated() != null) {
                    existingCalenda.setDateCreated(calenda.getDateCreated());
                }
                if (calenda.getLastModifiedDate() != null) {
                    existingCalenda.setLastModifiedDate(calenda.getLastModifiedDate());
                }

                return existingCalenda;
            })
            .map(calendaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Calenda> findAll(Pageable pageable) {
        log.debug("Request to get all Calendas");
        return calendaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calenda> findOne(Long id) {
        log.debug("Request to get Calenda : {}", id);
        return calendaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calenda : {}", id);
        calendaRepository.deleteById(id);
    }
}
