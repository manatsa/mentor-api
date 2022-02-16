package com.mana.mentor.web.rest;

import com.mana.mentor.domain.StudentExams;
import com.mana.mentor.repository.StudentExamsRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.StudentExamsService;
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
 * REST controller for managing {@link com.mana.mentor.domain.StudentExams}.
 */
@RestController
@RequestMapping("/api")
public class StudentExamsResource {

    private final Logger log = LoggerFactory.getLogger(StudentExamsResource.class);

    private static final String ENTITY_NAME = "studentExams";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentExamsService studentExamsService;

    private final StudentExamsRepository studentExamsRepository;

    public StudentExamsResource(StudentExamsService studentExamsService, StudentExamsRepository studentExamsRepository) {
        this.studentExamsService = studentExamsService;
        this.studentExamsRepository = studentExamsRepository;
    }

    /**
     * {@code POST  /student-exams} : Create a new studentExams.
     *
     * @param studentExams the studentExams to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentExams, or with status {@code 400 (Bad Request)} if the studentExams has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-exams")
    public ResponseEntity<StudentExams> createStudentExams(@Valid @RequestBody StudentExams studentExams) throws URISyntaxException {
        log.debug("REST request to save StudentExams : {}", studentExams);
        if (studentExams.getId() != null) {
            throw new BadRequestAlertException("A new studentExams cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentExams result = studentExamsService.save(studentExams);
        return ResponseEntity
            .created(new URI("/api/student-exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-exams/:id} : Updates an existing studentExams.
     *
     * @param id the id of the studentExams to save.
     * @param studentExams the studentExams to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentExams,
     * or with status {@code 400 (Bad Request)} if the studentExams is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentExams couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-exams/{id}")
    public ResponseEntity<StudentExams> updateStudentExams(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentExams studentExams
    ) throws URISyntaxException {
        log.debug("REST request to update StudentExams : {}, {}", id, studentExams);
        if (studentExams.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentExams.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentExamsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentExams result = studentExamsService.save(studentExams);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentExams.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-exams/:id} : Partial updates given fields of an existing studentExams, field will ignore if it is null
     *
     * @param id the id of the studentExams to save.
     * @param studentExams the studentExams to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentExams,
     * or with status {@code 400 (Bad Request)} if the studentExams is not valid,
     * or with status {@code 404 (Not Found)} if the studentExams is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentExams couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-exams/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentExams> partialUpdateStudentExams(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentExams studentExams
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentExams partially : {}, {}", id, studentExams);
        if (studentExams.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentExams.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentExamsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentExams> result = studentExamsService.partialUpdate(studentExams);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentExams.getId().toString())
        );
    }

    /**
     * {@code GET  /student-exams} : get all the studentExams.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentExams in body.
     */
    @GetMapping("/student-exams")
    public ResponseEntity<PageResponseVM> getAllStudentExams(Pageable pageable) {
        log.debug("REST request to get a page of StudentExams");
        Page<StudentExams> page = studentExamsService.findAll(pageable);
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
     * {@code GET  /student-exams/:id} : get the "id" studentExams.
     *
     * @param id the id of the studentExams to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentExams, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-exams/{id}")
    public ResponseEntity<StudentExams> getStudentExams(@PathVariable Long id) {
        log.debug("REST request to get StudentExams : {}", id);
        Optional<StudentExams> studentExams = studentExamsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentExams);
    }

    /**
     * {@code DELETE  /student-exams/:id} : delete the "id" studentExams.
     *
     * @param id the id of the studentExams to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-exams/{id}")
    public ResponseEntity<Void> deleteStudentExams(@PathVariable Long id) {
        log.debug("REST request to delete StudentExams : {}", id);
        studentExamsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
