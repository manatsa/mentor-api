package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExamAnswer;
import com.mana.mentor.repository.ExamAnswerRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExamAnswerService;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExamAnswer}.
 */
@RestController
@RequestMapping("/api")
public class ExamAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ExamAnswerResource.class);

    private static final String ENTITY_NAME = "examAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamAnswerService examAnswerService;

    private final ExamAnswerRepository examAnswerRepository;

    public ExamAnswerResource(ExamAnswerService examAnswerService, ExamAnswerRepository examAnswerRepository) {
        this.examAnswerService = examAnswerService;
        this.examAnswerRepository = examAnswerRepository;
    }

    /**
     * {@code POST  /exam-answers} : Create a new examAnswer.
     *
     * @param examAnswer the examAnswer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examAnswer, or with status {@code 400 (Bad Request)} if the examAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-answers")
    public ResponseEntity<ExamAnswer> createExamAnswer(@Valid @RequestBody ExamAnswer examAnswer) throws URISyntaxException {
        log.debug("REST request to save ExamAnswer : {}", examAnswer);
        if (examAnswer.getId() != null) {
            throw new BadRequestAlertException("A new examAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamAnswer result = examAnswerService.save(examAnswer);
        return ResponseEntity
            .created(new URI("/api/exam-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-answers/:id} : Updates an existing examAnswer.
     *
     * @param id the id of the examAnswer to save.
     * @param examAnswer the examAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examAnswer,
     * or with status {@code 400 (Bad Request)} if the examAnswer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-answers/{id}")
    public ResponseEntity<ExamAnswer> updateExamAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamAnswer examAnswer
    ) throws URISyntaxException {
        log.debug("REST request to update ExamAnswer : {}, {}", id, examAnswer);
        if (examAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamAnswer result = examAnswerService.save(examAnswer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examAnswer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-answers/:id} : Partial updates given fields of an existing examAnswer, field will ignore if it is null
     *
     * @param id the id of the examAnswer to save.
     * @param examAnswer the examAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examAnswer,
     * or with status {@code 400 (Bad Request)} if the examAnswer is not valid,
     * or with status {@code 404 (Not Found)} if the examAnswer is not found,
     * or with status {@code 500 (Internal Server Error)} if the examAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamAnswer> partialUpdateExamAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamAnswer examAnswer
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamAnswer partially : {}, {}", id, examAnswer);
        if (examAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamAnswer> result = examAnswerService.partialUpdate(examAnswer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examAnswer.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-answers} : get all the examAnswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examAnswers in body.
     */
    @GetMapping("/exam-answers")
    public ResponseEntity<PageResponseVM> getAllExamAnswers(Pageable pageable) {
        log.debug("REST request to get a page of ExamAnswers");
        Page<ExamAnswer> page = examAnswerService.findAll(pageable);
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
     * {@code GET  /exam-answers/:id} : get the "id" examAnswer.
     *
     * @param id the id of the examAnswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examAnswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-answers/{id}")
    public ResponseEntity<ExamAnswer> getExamAnswer(@PathVariable Long id) {
        log.debug("REST request to get ExamAnswer : {}", id);
        Optional<ExamAnswer> examAnswer = examAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examAnswer);
    }

    /**
     * {@code DELETE  /exam-answers/:id} : delete the "id" examAnswer.
     *
     * @param id the id of the examAnswer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-answers/{id}")
    public ResponseEntity<Void> deleteExamAnswer(@PathVariable Long id) {
        log.debug("REST request to delete ExamAnswer : {}", id);
        examAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
