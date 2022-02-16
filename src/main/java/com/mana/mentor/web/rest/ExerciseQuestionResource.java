package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExerciseQuestion;
import com.mana.mentor.repository.ExerciseQuestionRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExerciseQuestionService;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExerciseQuestion}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseQuestionResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseQuestionResource.class);

    private static final String ENTITY_NAME = "exerciseQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseQuestionService exerciseQuestionService;

    private final ExerciseQuestionRepository exerciseQuestionRepository;

    public ExerciseQuestionResource(
        ExerciseQuestionService exerciseQuestionService,
        ExerciseQuestionRepository exerciseQuestionRepository
    ) {
        this.exerciseQuestionService = exerciseQuestionService;
        this.exerciseQuestionRepository = exerciseQuestionRepository;
    }

    /**
     * {@code POST  /exercise-questions} : Create a new exerciseQuestion.
     *
     * @param exerciseQuestion the exerciseQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseQuestion, or with status {@code 400 (Bad Request)} if the exerciseQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-questions")
    public ResponseEntity<ExerciseQuestion> createExerciseQuestion(@Valid @RequestBody ExerciseQuestion exerciseQuestion)
        throws URISyntaxException {
        log.debug("REST request to save ExerciseQuestion : {}", exerciseQuestion);
        if (exerciseQuestion.getId() != null) {
            throw new BadRequestAlertException("A new exerciseQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseQuestion result = exerciseQuestionService.save(exerciseQuestion);
        return ResponseEntity
            .created(new URI("/api/exercise-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-questions/:id} : Updates an existing exerciseQuestion.
     *
     * @param id the id of the exerciseQuestion to save.
     * @param exerciseQuestion the exerciseQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseQuestion,
     * or with status {@code 400 (Bad Request)} if the exerciseQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-questions/{id}")
    public ResponseEntity<ExerciseQuestion> updateExerciseQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseQuestion exerciseQuestion
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseQuestion : {}, {}", id, exerciseQuestion);
        if (exerciseQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseQuestion result = exerciseQuestionService.save(exerciseQuestion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-questions/:id} : Partial updates given fields of an existing exerciseQuestion, field will ignore if it is null
     *
     * @param id the id of the exerciseQuestion to save.
     * @param exerciseQuestion the exerciseQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseQuestion,
     * or with status {@code 400 (Bad Request)} if the exerciseQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExerciseQuestion> partialUpdateExerciseQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseQuestion exerciseQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseQuestion partially : {}, {}", id, exerciseQuestion);
        if (exerciseQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseQuestion> result = exerciseQuestionService.partialUpdate(exerciseQuestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-questions} : get all the exerciseQuestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseQuestions in body.
     */
    @GetMapping("/exercise-questions")
    public ResponseEntity<PageResponseVM> getAllExerciseQuestions(Pageable pageable) {
        log.debug("REST request to get a page of ExerciseQuestions");
        Page<ExerciseQuestion> page = exerciseQuestionService.findAll(pageable);
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
     * {@code GET  /exercise-questions/:id} : get the "id" exerciseQuestion.
     *
     * @param id the id of the exerciseQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-questions/{id}")
    public ResponseEntity<ExerciseQuestion> getExerciseQuestion(@PathVariable Long id) {
        log.debug("REST request to get ExerciseQuestion : {}", id);
        Optional<ExerciseQuestion> exerciseQuestion = exerciseQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseQuestion);
    }

    /**
     * {@code DELETE  /exercise-questions/:id} : delete the "id" exerciseQuestion.
     *
     * @param id the id of the exerciseQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-questions/{id}")
    public ResponseEntity<Void> deleteExerciseQuestion(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseQuestion : {}", id);
        exerciseQuestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
