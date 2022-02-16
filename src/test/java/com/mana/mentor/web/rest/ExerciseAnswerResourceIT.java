package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExerciseAnswer;
import com.mana.mentor.repository.ExerciseAnswerRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExerciseAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseAnswerResourceIT {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exercise-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseAnswerRepository exerciseAnswerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseAnswerMockMvc;

    private ExerciseAnswer exerciseAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseAnswer createEntity(EntityManager em) {
        ExerciseAnswer exerciseAnswer = new ExerciseAnswer()
            .answer(DEFAULT_ANSWER)
            .explanation(DEFAULT_EXPLANATION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return exerciseAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseAnswer createUpdatedEntity(EntityManager em) {
        ExerciseAnswer exerciseAnswer = new ExerciseAnswer()
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return exerciseAnswer;
    }

    @BeforeEach
    public void initTest() {
        exerciseAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseAnswer() throws Exception {
        int databaseSizeBeforeCreate = exerciseAnswerRepository.findAll().size();
        // Create the ExerciseAnswer
        restExerciseAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseAnswer testExerciseAnswer = exerciseAnswerList.get(exerciseAnswerList.size() - 1);
        assertThat(testExerciseAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExerciseAnswer.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testExerciseAnswer.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseAnswer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExerciseAnswerWithExistingId() throws Exception {
        // Create the ExerciseAnswer with an existing ID
        exerciseAnswer.setId(1L);

        int databaseSizeBeforeCreate = exerciseAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseAnswerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExerciseAnswers() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        // Get all the exerciseAnswerList
        restExerciseAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExerciseAnswer() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        // Get the exerciseAnswer
        restExerciseAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExerciseAnswer() throws Exception {
        // Get the exerciseAnswer
        restExerciseAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseAnswer() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();

        // Update the exerciseAnswer
        ExerciseAnswer updatedExerciseAnswer = exerciseAnswerRepository.findById(exerciseAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseAnswer are not directly saved in db
        em.detach(updatedExerciseAnswer);
        updatedExerciseAnswer
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExerciseAnswer testExerciseAnswer = exerciseAnswerList.get(exerciseAnswerList.size() - 1);
        assertThat(testExerciseAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExerciseAnswer.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExerciseAnswer.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseAnswer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseAnswer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseAnswerWithPatch() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();

        // Update the exerciseAnswer using partial update
        ExerciseAnswer partialUpdatedExerciseAnswer = new ExerciseAnswer();
        partialUpdatedExerciseAnswer.setId(exerciseAnswer.getId());

        partialUpdatedExerciseAnswer.explanation(UPDATED_EXPLANATION);

        restExerciseAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExerciseAnswer testExerciseAnswer = exerciseAnswerList.get(exerciseAnswerList.size() - 1);
        assertThat(testExerciseAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExerciseAnswer.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExerciseAnswer.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseAnswer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExerciseAnswerWithPatch() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();

        // Update the exerciseAnswer using partial update
        ExerciseAnswer partialUpdatedExerciseAnswer = new ExerciseAnswer();
        partialUpdatedExerciseAnswer.setId(exerciseAnswer.getId());

        partialUpdatedExerciseAnswer
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExerciseAnswer testExerciseAnswer = exerciseAnswerList.get(exerciseAnswerList.size() - 1);
        assertThat(testExerciseAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExerciseAnswer.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExerciseAnswer.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseAnswer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = exerciseAnswerRepository.findAll().size();
        exerciseAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseAnswer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseAnswer in the database
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseAnswer() throws Exception {
        // Initialize the database
        exerciseAnswerRepository.saveAndFlush(exerciseAnswer);

        int databaseSizeBeforeDelete = exerciseAnswerRepository.findAll().size();

        // Delete the exerciseAnswer
        restExerciseAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseAnswer> exerciseAnswerList = exerciseAnswerRepository.findAll();
        assertThat(exerciseAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
