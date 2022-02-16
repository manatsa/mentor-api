package com.mana.mentor.web.rest;

import com.mana.mentor.domain.Guardian;
import com.mana.mentor.repository.GuardianRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.GuardianService;
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
 * REST controller for managing {@link com.mana.mentor.domain.Guardian}.
 */
@RestController
@RequestMapping("/api")
public class GuardianResource {

    private final Logger log = LoggerFactory.getLogger(GuardianResource.class);

    private static final String ENTITY_NAME = "guardian";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuardianService guardianService;

    private final GuardianRepository guardianRepository;

    public GuardianResource(GuardianService guardianService, GuardianRepository guardianRepository) {
        this.guardianService = guardianService;
        this.guardianRepository = guardianRepository;
    }

    /**
     * {@code POST  /guardians} : Create a new guardian.
     *
     * @param guardian the guardian to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guardian, or with status {@code 400 (Bad Request)} if the guardian has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guardians")
    public ResponseEntity<Guardian> createGuardian(@Valid @RequestBody Guardian guardian) throws URISyntaxException {
        log.debug("REST request to save Guardian : {}", guardian);
        if (guardian.getId() != null) {
            throw new BadRequestAlertException("A new guardian cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Guardian result = guardianService.save(guardian);
        return ResponseEntity
            .created(new URI("/api/guardians/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guardians/:id} : Updates an existing guardian.
     *
     * @param id the id of the guardian to save.
     * @param guardian the guardian to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardian,
     * or with status {@code 400 (Bad Request)} if the guardian is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guardian couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guardians/{id}")
    public ResponseEntity<Guardian> updateGuardian(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Guardian guardian
    ) throws URISyntaxException {
        log.debug("REST request to update Guardian : {}, {}", id, guardian);
        if (guardian.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardian.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Guardian result = guardianService.save(guardian);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, guardian.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guardians/:id} : Partial updates given fields of an existing guardian, field will ignore if it is null
     *
     * @param id the id of the guardian to save.
     * @param guardian the guardian to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardian,
     * or with status {@code 400 (Bad Request)} if the guardian is not valid,
     * or with status {@code 404 (Not Found)} if the guardian is not found,
     * or with status {@code 500 (Internal Server Error)} if the guardian couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guardians/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Guardian> partialUpdateGuardian(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Guardian guardian
    ) throws URISyntaxException {
        log.debug("REST request to partial update Guardian partially : {}, {}", id, guardian);
        if (guardian.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardian.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Guardian> result = guardianService.partialUpdate(guardian);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, guardian.getId().toString())
        );
    }

    /**
     * {@code GET  /guardians} : get all the guardians.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guardians in body.
     */
    @GetMapping("/guardians")
    public ResponseEntity<PageResponseVM> getAllGuardians(Pageable pageable) {
        log.debug("REST request to get a page of Guardians");
        Page<Guardian> page = guardianService.findAll(pageable);
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
     * {@code GET  /guardians/:id} : get the "id" guardian.
     *
     * @param id the id of the guardian to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guardian, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guardians/{id}")
    public ResponseEntity<Guardian> getGuardian(@PathVariable Long id) {
        log.debug("REST request to get Guardian : {}", id);
        Optional<Guardian> guardian = guardianService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guardian);
    }

    /**
     * {@code DELETE  /guardians/:id} : delete the "id" guardian.
     *
     * @param id the id of the guardian to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guardians/{id}")
    public ResponseEntity<Void> deleteGuardian(@PathVariable Long id) {
        log.debug("REST request to delete Guardian : {}", id);
        guardianService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
