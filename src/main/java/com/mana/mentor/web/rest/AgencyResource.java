package com.mana.mentor.web.rest;

import com.mana.mentor.domain.Agency;
import com.mana.mentor.repository.AgencyRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.AgencyService;
import com.mana.mentor.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mana.mentor.domain.Agency}.
 */
@RestController
@RequestMapping("/api")
public class AgencyResource {

    private final Logger log = LoggerFactory.getLogger(AgencyResource.class);

    private static final String ENTITY_NAME = "agency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyService agencyService;

    private final AgencyRepository agencyRepository;

    public AgencyResource(AgencyService agencyService, AgencyRepository agencyRepository) {
        this.agencyService = agencyService;
        this.agencyRepository = agencyRepository;
    }

    /**
     * {@code POST  /agencies} : Create a new agency.
     *
     * @param agency the agency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agency, or with status {@code 400 (Bad Request)} if the agency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agencies")
    public ResponseEntity<Agency> createAgency(@Valid @RequestBody Agency agency) throws URISyntaxException {
        log.debug("REST request to save Agency : {}", agency);
        if (agency.getId() != null) {
            throw new BadRequestAlertException("A new agency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agency result = agencyService.save(agency);
        return ResponseEntity
            .created(new URI("/api/agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agencies/:id} : Updates an existing agency.
     *
     * @param id the id of the agency to save.
     * @param agency the agency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agency,
     * or with status {@code 400 (Bad Request)} if the agency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agencies/{id}")
    public ResponseEntity<Agency> updateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Agency agency
    ) throws URISyntaxException {
        log.debug("REST request to update Agency : {}, {}", id, agency);
        if (agency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Agency result = agencyService.save(agency);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agency.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agencies/:id} : Partial updates given fields of an existing agency, field will ignore if it is null
     *
     * @param id the id of the agency to save.
     * @param agency the agency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agency,
     * or with status {@code 400 (Bad Request)} if the agency is not valid,
     * or with status {@code 404 (Not Found)} if the agency is not found,
     * or with status {@code 500 (Internal Server Error)} if the agency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Agency> partialUpdateAgency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Agency agency
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agency partially : {}, {}", id, agency);
        if (agency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agency> result = agencyService.partialUpdate(agency);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agency.getId().toString())
        );
    }

    /**
     * {@code GET  /agencies} : get all the agencies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencies in body.
     */
    @GetMapping("/agencies")
    public ResponseEntity<PageResponseVM> getAllAgencies(Pageable pageable) {
        log.debug("REST request to get a page of Agencies");
        Page<Agency> page = agencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        PageResponseVM pageResponseVM = new PageResponseVM(
            page.getContent(),
            page.getTotalElements(),
            page.getNumber(),
            page.getTotalPages()
        );
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        return ResponseEntity.ok().headers(headers).body(pageResponseVM);
    }

    /**
     * {@code GET  /agencies/:id} : get the "id" agency.
     *
     * @param id the id of the agency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agencies/{id}")
    public ResponseEntity<Agency> getAgency(@PathVariable Long id) {
        log.debug("REST request to get Agency : {}", id);
        Optional<Agency> agency = agencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agency);
    }

    /**
     * {@code DELETE  /agencies/:id} : delete the "id" agency.
     *
     * @param id the id of the agency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agencies/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        log.debug("REST request to delete Agency : {}", id);
        agencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
