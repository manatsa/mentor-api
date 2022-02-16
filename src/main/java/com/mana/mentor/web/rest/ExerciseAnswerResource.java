package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExerciseAnswer;
import com.mana.mentor.repository.ExerciseAnswerRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExerciseAnswerService;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExerciseAnswer}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseAnswerResource.class);

    private static final String ENTITY_NAME = "exerciseAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseAnswerService exerciseAnswerService;

    private final ExerciseAnswerRepository exerciseAnswerRepository;

    public ExerciseAnswerResource(ExerciseAnswerService exerciseAnswerService, ExerciseAnswerRepository exerciseAnswerRepository) {
        this.exerciseAnswerService = exerciseAnswerService;
        this.exerciseAnswerRepository = exerciseAnswerRepository;
    }

    /**
     * {@code POST  /exercise-answers} : Create a new exerciseAnswer.
     *
     * @param exerciseAnswer the exerciseAnswer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseAnswer, or with status {@code 400 (Bad Request)} if the exerciseAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-answers")
    public ResponseEntity<ExerciseAnswer> createExerciseAnswer(@Valid @RequestBody ExerciseAnswer exerciseAnswer)
        throws URISyntaxException {
        log.debug("REST request to save ExerciseAnswer : {}", exerciseAnswer);
        if (exerciseAnswer.getId() != null) {
            throw new BadRequestAlertException("A new exerciseAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseAnswer result = exerciseAnswerService.save(exerciseAnswer);
        return ResponseEntity
            .created(new URI("/api/exercise-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-answers/:id} : Updates an existing exerciseAnswer.
     *
     * @param id the id of the exerciseAnswer to save.
     * @param exerciseAnswer the exerciseAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseAnswer,
     * or with status {@code 400 (Bad Request)} if the exerciseAnswer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-answers/{id}")
    public ResponseEntity<ExerciseAnswer> updateExerciseAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExerciseAnswer exerciseAnswer
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseAnswer : {}, {}", id, exerciseAnswer);
        if (exerciseAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseAnswer result = exerciseAnswerService.save(exerciseAnswer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseAnswer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-answers/:id} : Partial updates given fields of an existing exerciseAnswer, field will ignore if it is null
     *
     * @param id the id of the exerciseAnswer to save.
     * @param exerciseAnswer the exerciseAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseAnswer,
     * or with status {@code 400 (Bad Request)} if the exerciseAnswer is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseAnswer is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExerciseAnswer> partialUpdateExerciseAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExerciseAnswer exerciseAnswer
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseAnswer partially : {}, {}", id, exerciseAnswer);
        if (exerciseAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseAnswer> result = exerciseAnswerService.partialUpdate(exerciseAnswer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseAnswer.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-answers} : get all the exerciseAnswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseAnswers in body.
     */
    @GetMapping("/exercise-answers")
    public ResponseEntity<PageResponseVM> getAllExerciseAnswers(Pageable pageable) {
        log.debug("REST request to get a page of ExerciseAnswers");
        Page<ExerciseAnswer> page = exerciseAnswerService.findAll(pageable);
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
     * {@code GET  /exercise-answers/:id} : get the "id" exerciseAnswer.
     *
     * @param id the id of the exerciseAnswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseAnswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-answers/{id}")
    public ResponseEntity<ExerciseAnswer> getExerciseAnswer(@PathVariable Long id) {
        log.debug("REST request to get ExerciseAnswer : {}", id);
        Optional<ExerciseAnswer> exerciseAnswer = exerciseAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseAnswer);
    }

    /**
     * {@code DELETE  /exercise-answers/:id} : delete the "id" exerciseAnswer.
     *
     * @param id the id of the exerciseAnswer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-answers/{id}")
    public ResponseEntity<Void> deleteExerciseAnswer(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseAnswer : {}", id);
        exerciseAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
