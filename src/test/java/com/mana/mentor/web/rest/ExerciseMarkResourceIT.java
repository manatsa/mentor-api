package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExerciseMark;
import com.mana.mentor.repository.ExerciseMarkRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExerciseMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseMarkResourceIT {

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exercise-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseMarkRepository exerciseMarkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseMarkMockMvc;

    private ExerciseMark exerciseMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseMark createEntity(EntityManager em) {
        ExerciseMark exerciseMark = new ExerciseMark()
            .total(DEFAULT_TOTAL)
            .mark(DEFAULT_MARK)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return exerciseMark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseMark createUpdatedEntity(EntityManager em) {
        ExerciseMark exerciseMark = new ExerciseMark()
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return exerciseMark;
    }

    @BeforeEach
    public void initTest() {
        exerciseMark = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseMark() throws Exception {
        int databaseSizeBeforeCreate = exerciseMarkRepository.findAll().size();
        // Create the ExerciseMark
        restExerciseMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseMark)))
            .andExpect(status().isCreated());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseMark testExerciseMark = exerciseMarkList.get(exerciseMarkList.size() - 1);
        assertThat(testExerciseMark.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testExerciseMark.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testExerciseMark.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExerciseMarkWithExistingId() throws Exception {
        // Create the ExerciseMark with an existing ID
        exerciseMark.setId(1L);

        int databaseSizeBeforeCreate = exerciseMarkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseMark)))
            .andExpect(status().isBadRequest());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExerciseMarks() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        // Get all the exerciseMarkList
        restExerciseMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExerciseMark() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        // Get the exerciseMark
        restExerciseMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseMark.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExerciseMark() throws Exception {
        // Get the exerciseMark
        restExerciseMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseMark() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();

        // Update the exerciseMark
        ExerciseMark updatedExerciseMark = exerciseMarkRepository.findById(exerciseMark.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseMark are not directly saved in db
        em.detach(updatedExerciseMark);
        updatedExerciseMark
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseMark.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseMark))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
        ExerciseMark testExerciseMark = exerciseMarkList.get(exerciseMarkList.size() - 1);
        assertThat(testExerciseMark.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testExerciseMark.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testExerciseMark.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseMark.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseMark)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseMarkWithPatch() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();

        // Update the exerciseMark using partial update
        ExerciseMark partialUpdatedExerciseMark = new ExerciseMark();
        partialUpdatedExerciseMark.setId(exerciseMark.getId());

        partialUpdatedExerciseMark.total(UPDATED_TOTAL).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseMark))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
        ExerciseMark testExerciseMark = exerciseMarkList.get(exerciseMarkList.size() - 1);
        assertThat(testExerciseMark.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testExerciseMark.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testExerciseMark.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExerciseMarkWithPatch() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();

        // Update the exerciseMark using partial update
        ExerciseMark partialUpdatedExerciseMark = new ExerciseMark();
        partialUpdatedExerciseMark.setId(exerciseMark.getId());

        partialUpdatedExerciseMark
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseMark))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
        ExerciseMark testExerciseMark = exerciseMarkList.get(exerciseMarkList.size() - 1);
        assertThat(testExerciseMark.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testExerciseMark.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testExerciseMark.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseMark() throws Exception {
        int databaseSizeBeforeUpdate = exerciseMarkRepository.findAll().size();
        exerciseMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMarkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseMark))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseMark in the database
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseMark() throws Exception {
        // Initialize the database
        exerciseMarkRepository.saveAndFlush(exerciseMark);

        int databaseSizeBeforeDelete = exerciseMarkRepository.findAll().size();

        // Delete the exerciseMark
        restExerciseMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseMark> exerciseMarkList = exerciseMarkRepository.findAll();
        assertThat(exerciseMarkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
