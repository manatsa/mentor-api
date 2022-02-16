package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExamQuestion;
import com.mana.mentor.repository.ExamQuestionRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExamQuestionService;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExamQuestion}.
 */
@RestController
@RequestMapping("/api")
public class ExamQuestionResource {

    private final Logger log = LoggerFactory.getLogger(ExamQuestionResource.class);

    private static final String ENTITY_NAME = "examQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamQuestionService examQuestionService;

    private final ExamQuestionRepository examQuestionRepository;

    public ExamQuestionResource(ExamQuestionService examQuestionService, ExamQuestionRepository examQuestionRepository) {
        this.examQuestionService = examQuestionService;
        this.examQuestionRepository = examQuestionRepository;
    }

    /**
     * {@code POST  /exam-questions} : Create a new examQuestion.
     *
     * @param examQuestion the examQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examQuestion, or with status {@code 400 (Bad Request)} if the examQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-questions")
    public ResponseEntity<ExamQuestion> createExamQuestion(@Valid @RequestBody ExamQuestion examQuestion) throws URISyntaxException {
        log.debug("REST request to save ExamQuestion : {}", examQuestion);
        if (examQuestion.getId() != null) {
            throw new BadRequestAlertException("A new examQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamQuestion result = examQuestionService.save(examQuestion);
        return ResponseEntity
            .created(new URI("/api/exam-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-questions/:id} : Updates an existing examQuestion.
     *
     * @param id the id of the examQuestion to save.
     * @param examQuestion the examQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examQuestion,
     * or with status {@code 400 (Bad Request)} if the examQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-questions/{id}")
    public ResponseEntity<ExamQuestion> updateExamQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamQuestion examQuestion
    ) throws URISyntaxException {
        log.debug("REST request to update ExamQuestion : {}, {}", id, examQuestion);
        if (examQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamQuestion result = examQuestionService.save(examQuestion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-questions/:id} : Partial updates given fields of an existing examQuestion, field will ignore if it is null
     *
     * @param id the id of the examQuestion to save.
     * @param examQuestion the examQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examQuestion,
     * or with status {@code 400 (Bad Request)} if the examQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the examQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the examQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamQuestion> partialUpdateExamQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamQuestion examQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamQuestion partially : {}, {}", id, examQuestion);
        if (examQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamQuestion> result = examQuestionService.partialUpdate(examQuestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-questions} : get all the examQuestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examQuestions in body.
     */
    @GetMapping("/exam-questions")
    public ResponseEntity<PageResponseVM> getAllExamQuestions(Pageable pageable) {
        log.debug("REST request to get a page of ExamQuestions");
        Page<ExamQuestion> page = examQuestionService.findAll(pageable);
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
     * {@code GET  /exam-questions/:id} : get the "id" examQuestion.
     *
     * @param id the id of the examQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-questions/{id}")
    public ResponseEntity<ExamQuestion> getExamQuestion(@PathVariable Long id) {
        log.debug("REST request to get ExamQuestion : {}", id);
        Optional<ExamQuestion> examQuestion = examQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examQuestion);
    }

    /**
     * {@code DELETE  /exam-questions/:id} : delete the "id" examQuestion.
     *
     * @param id the id of the examQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-questions/{id}")
    public ResponseEntity<Void> deleteExamQuestion(@PathVariable Long id) {
        log.debug("REST request to delete ExamQuestion : {}", id);
        examQuestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
