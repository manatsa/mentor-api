package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExamMark;
import com.mana.mentor.repository.ExamMarkRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExamMarkService;
import com.mana.mentor.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExamMark}.
 */
@RestController
@RequestMapping("/api")
public class ExamMarkResource {

    private final Logger log = LoggerFactory.getLogger(ExamMarkResource.class);

    private static final String ENTITY_NAME = "examMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamMarkService examMarkService;

    private final ExamMarkRepository examMarkRepository;

    public ExamMarkResource(ExamMarkService examMarkService, ExamMarkRepository examMarkRepository) {
        this.examMarkService = examMarkService;
        this.examMarkRepository = examMarkRepository;
    }

    /**
     * {@code POST  /exam-marks} : Create a new examMark.
     *
     * @param examMark the examMark to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examMark, or with status {@code 400 (Bad Request)} if the examMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-marks")
    public ResponseEntity<ExamMark> createExamMark(@RequestBody ExamMark examMark) throws URISyntaxException {
        log.debug("REST request to save ExamMark : {}", examMark);
        if (examMark.getId() != null) {
            throw new BadRequestAlertException("A new examMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamMark result = examMarkService.save(examMark);
        return ResponseEntity
            .created(new URI("/api/exam-marks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-marks/:id} : Updates an existing examMark.
     *
     * @param id the id of the examMark to save.
     * @param examMark the examMark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examMark,
     * or with status {@code 400 (Bad Request)} if the examMark is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examMark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-marks/{id}")
    public ResponseEntity<ExamMark> updateExamMark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExamMark examMark
    ) throws URISyntaxException {
        log.debug("REST request to update ExamMark : {}, {}", id, examMark);
        if (examMark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examMark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamMark result = examMarkService.save(examMark);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examMark.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-marks/:id} : Partial updates given fields of an existing examMark, field will ignore if it is null
     *
     * @param id the id of the examMark to save.
     * @param examMark the examMark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examMark,
     * or with status {@code 400 (Bad Request)} if the examMark is not valid,
     * or with status {@code 404 (Not Found)} if the examMark is not found,
     * or with status {@code 500 (Internal Server Error)} if the examMark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-marks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamMark> partialUpdateExamMark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExamMark examMark
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamMark partially : {}, {}", id, examMark);
        if (examMark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examMark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamMark> result = examMarkService.partialUpdate(examMark);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examMark.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-marks} : get all the examMarks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examMarks in body.
     */
    @GetMapping("/exam-marks")
    public ResponseEntity<PageResponseVM> getAllExamMarks(Pageable pageable) {
        log.debug("REST request to get a page of ExamMarks");
        Page<ExamMark> page = examMarkService.findAll(pageable);
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
     * {@code GET  /exam-marks/:id} : get the "id" examMark.
     *
     * @param id the id of the examMark to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examMark, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-marks/{id}")
    public ResponseEntity<ExamMark> getExamMark(@PathVariable Long id) {
        log.debug("REST request to get ExamMark : {}", id);
        Optional<ExamMark> examMark = examMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examMark);
    }

    /**
     * {@code DELETE  /exam-marks/:id} : delete the "id" examMark.
     *
     * @param id the id of the examMark to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-marks/{id}")
    public ResponseEntity<Void> deleteExamMark(@PathVariable Long id) {
        log.debug("REST request to delete ExamMark : {}", id);
        examMarkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
