package com.mana.mentor.web.rest;

import com.mana.mentor.domain.Calenda;
import com.mana.mentor.repository.CalendaRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.CalendaService;
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
 * REST controller for managing {@link com.mana.mentor.domain.Calenda}.
 */
@RestController
@RequestMapping("/api")
public class CalendaResource {

    private final Logger log = LoggerFactory.getLogger(CalendaResource.class);

    private static final String ENTITY_NAME = "calenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendaService calendaService;

    private final CalendaRepository calendaRepository;

    public CalendaResource(CalendaService calendaService, CalendaRepository calendaRepository) {
        this.calendaService = calendaService;
        this.calendaRepository = calendaRepository;
    }

    /**
     * {@code POST  /calendas} : Create a new calenda.
     *
     * @param calenda the calenda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calenda, or with status {@code 400 (Bad Request)} if the calenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendas")
    public ResponseEntity<Calenda> createCalenda(@Valid @RequestBody Calenda calenda) throws URISyntaxException {
        log.debug("REST request to save Calenda : {}", calenda);
        if (calenda.getId() != null) {
            throw new BadRequestAlertException("A new calenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Calenda result = calendaService.save(calenda);
        return ResponseEntity
            .created(new URI("/api/calendas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendas/:id} : Updates an existing calenda.
     *
     * @param id the id of the calenda to save.
     * @param calenda the calenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calenda,
     * or with status {@code 400 (Bad Request)} if the calenda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendas/{id}")
    public ResponseEntity<Calenda> updateCalenda(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Calenda calenda
    ) throws URISyntaxException {
        log.debug("REST request to update Calenda : {}, {}", id, calenda);
        if (calenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Calenda result = calendaService.save(calenda);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calenda.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calendas/:id} : Partial updates given fields of an existing calenda, field will ignore if it is null
     *
     * @param id the id of the calenda to save.
     * @param calenda the calenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calenda,
     * or with status {@code 400 (Bad Request)} if the calenda is not valid,
     * or with status {@code 404 (Not Found)} if the calenda is not found,
     * or with status {@code 500 (Internal Server Error)} if the calenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calendas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Calenda> partialUpdateCalenda(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Calenda calenda
    ) throws URISyntaxException {
        log.debug("REST request to partial update Calenda partially : {}, {}", id, calenda);
        if (calenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Calenda> result = calendaService.partialUpdate(calenda);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, calenda.getId().toString())
        );
    }

    /**
     * {@code GET  /calendas} : get all the calendas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendas in body.
     */
    @GetMapping("/calendas")
    public ResponseEntity<PageResponseVM> getAllCalendas(Pageable pageable) {
        log.debug("REST request to get a page of Calendas");
        Page<Calenda> page = calendaService.findAll(pageable);
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
     * {@code GET  /calendas/:id} : get the "id" calenda.
     *
     * @param id the id of the calenda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendas/{id}")
    public ResponseEntity<Calenda> getCalenda(@PathVariable Long id) {
        log.debug("REST request to get Calenda : {}", id);
        Optional<Calenda> calenda = calendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calenda);
    }

    /**
     * {@code DELETE  /calendas/:id} : delete the "id" calenda.
     *
     * @param id the id of the calenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendas/{id}")
    public ResponseEntity<Void> deleteCalenda(@PathVariable Long id) {
        log.debug("REST request to delete Calenda : {}", id);
        calendaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
