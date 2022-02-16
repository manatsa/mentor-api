package com.mana.mentor.web.rest;

import com.mana.mentor.domain.ExerciseMark;
import com.mana.mentor.repository.ExerciseMarkRepository;
import com.mana.mentor.responses.PageResponseVM;
import com.mana.mentor.service.ExerciseMarkService;
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
 * REST controller for managing {@link com.mana.mentor.domain.ExerciseMark}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseMarkResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseMarkResource.class);

    private static final String ENTITY_NAME = "exerciseMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseMarkService exerciseMarkService;

    private final ExerciseMarkRepository exerciseMarkRepository;

    public ExerciseMarkResource(ExerciseMarkService exerciseMarkService, ExerciseMarkRepository exerciseMarkRepository) {
        this.exerciseMarkService = exerciseMarkService;
        this.exerciseMarkRepository = exerciseMarkRepository;
    }

    /**
     * {@code POST  /exercise-marks} : Create a new exerciseMark.
     *
     * @param exerciseMark the exerciseMark to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseMark, or with status {@code 400 (Bad Request)} if the exerciseMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-marks")
    public ResponseEntity<ExerciseMark> createExerciseMark(@RequestBody ExerciseMark exerciseMark) throws URISyntaxException {
        log.debug("REST request to save ExerciseMark : {}", exerciseMark);
        if (exerciseMark.getId() != null) {
            throw new BadRequestAlertException("A new exerciseMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseMark result = exerciseMarkService.save(exerciseMark);
        return ResponseEntity
            .created(new URI("/api/exercise-marks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-marks/:id} : Updates an existing exerciseMark.
     *
     * @param id the id of the exerciseMark to save.
     * @param exerciseMark the exerciseMark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseMark,
     * or with status {@code 400 (Bad Request)} if the exerciseMark is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseMark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-marks/{id}")
    public ResponseEntity<ExerciseMark> updateExerciseMark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExerciseMark exerciseMark
    ) throws URISyntaxException {
        log.debug("REST request to update ExerciseMark : {}, {}", id, exerciseMark);
        if (exerciseMark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseMark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExerciseMark result = exerciseMarkService.save(exerciseMark);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseMark.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercise-marks/:id} : Partial updates given fields of an existing exerciseMark, field will ignore if it is null
     *
     * @param id the id of the exerciseMark to save.
     * @param exerciseMark the exerciseMark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseMark,
     * or with status {@code 400 (Bad Request)} if the exerciseMark is not valid,
     * or with status {@code 404 (Not Found)} if the exerciseMark is not found,
     * or with status {@code 500 (Internal Server Error)} if the exerciseMark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercise-marks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExerciseMark> partialUpdateExerciseMark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExerciseMark exerciseMark
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExerciseMark partially : {}, {}", id, exerciseMark);
        if (exerciseMark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exerciseMark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExerciseMark> result = exerciseMarkService.partialUpdate(exerciseMark);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciseMark.getId().toString())
        );
    }

    /**
     * {@code GET  /exercise-marks} : get all the exerciseMarks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseMarks in body.
     */
    @GetMapping("/exercise-marks")
    public ResponseEntity<PageResponseVM> getAllExerciseMarks(Pageable pageable) {
        log.debug("REST request to get a page of ExerciseMarks");
        Page<ExerciseMark> page = exerciseMarkService.findAll(pageable);
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
     * {@code GET  /exercise-marks/:id} : get the "id" exerciseMark.
     *
     * @param id the id of the exerciseMark to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseMark, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-marks/{id}")
    public ResponseEntity<ExerciseMark> getExerciseMark(@PathVariable Long id) {
        log.debug("REST request to get ExerciseMark : {}", id);
        Optional<ExerciseMark> exerciseMark = exerciseMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseMark);
    }

    /**
     * {@code DELETE  /exercise-marks/:id} : delete the "id" exerciseMark.
     *
     * @param id the id of the exerciseMark to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-marks/{id}")
    public ResponseEntity<Void> deleteExerciseMark(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseMark : {}", id);
        exerciseMarkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
