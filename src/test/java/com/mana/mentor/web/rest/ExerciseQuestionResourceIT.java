package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExerciseQuestion;
import com.mana.mentor.repository.ExerciseQuestionRepository;
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
 * Integration tests for the {@link ExerciseQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseQuestionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exercise-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseQuestionRepository exerciseQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseQuestionMockMvc;

    private ExerciseQuestion exerciseQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseQuestion createEntity(EntityManager em) {
        ExerciseQuestion exerciseQuestion = new ExerciseQuestion()
            .name(DEFAULT_NAME)
            .question(DEFAULT_QUESTION)
            .answer(DEFAULT_ANSWER)
            .explanation(DEFAULT_EXPLANATION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return exerciseQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseQuestion createUpdatedEntity(EntityManager em) {
        ExerciseQuestion exerciseQuestion = new ExerciseQuestion()
            .name(UPDATED_NAME)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return exerciseQuestion;
    }

    @BeforeEach
    public void initTest() {
        exerciseQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createExerciseQuestion() throws Exception {
        int databaseSizeBeforeCreate = exerciseQuestionRepository.findAll().size();
        // Create the ExerciseQuestion
        restExerciseQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isCreated());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseQuestion testExerciseQuestion = exerciseQuestionList.get(exerciseQuestionList.size() - 1);
        assertThat(testExerciseQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExerciseQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testExerciseQuestion.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExerciseQuestion.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testExerciseQuestion.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseQuestion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExerciseQuestionWithExistingId() throws Exception {
        // Create the ExerciseQuestion with an existing ID
        exerciseQuestion.setId(1L);

        int databaseSizeBeforeCreate = exerciseQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseQuestionRepository.findAll().size();
        // set the field null
        exerciseQuestion.setName(null);

        // Create the ExerciseQuestion, which fails.

        restExerciseQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseQuestionRepository.findAll().size();
        // set the field null
        exerciseQuestion.setQuestion(null);

        // Create the ExerciseQuestion, which fails.

        restExerciseQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseQuestionRepository.findAll().size();
        // set the field null
        exerciseQuestion.setAnswer(null);

        // Create the ExerciseQuestion, which fails.

        restExerciseQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExerciseQuestions() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        // Get all the exerciseQuestionList
        restExerciseQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExerciseQuestion() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        // Get the exerciseQuestion
        restExerciseQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, exerciseQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseQuestion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExerciseQuestion() throws Exception {
        // Get the exerciseQuestion
        restExerciseQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExerciseQuestion() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();

        // Update the exerciseQuestion
        ExerciseQuestion updatedExerciseQuestion = exerciseQuestionRepository.findById(exerciseQuestion.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseQuestion are not directly saved in db
        em.detach(updatedExerciseQuestion);
        updatedExerciseQuestion
            .name(UPDATED_NAME)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExerciseQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExerciseQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExerciseQuestion testExerciseQuestion = exerciseQuestionList.get(exerciseQuestionList.size() - 1);
        assertThat(testExerciseQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testExerciseQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExerciseQuestion.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExerciseQuestion.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseQuestionWithPatch() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();

        // Update the exerciseQuestion using partial update
        ExerciseQuestion partialUpdatedExerciseQuestion = new ExerciseQuestion();
        partialUpdatedExerciseQuestion.setId(exerciseQuestion.getId());

        partialUpdatedExerciseQuestion.name(UPDATED_NAME).answer(UPDATED_ANSWER).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExerciseQuestion testExerciseQuestion = exerciseQuestionList.get(exerciseQuestionList.size() - 1);
        assertThat(testExerciseQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testExerciseQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExerciseQuestion.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testExerciseQuestion.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExerciseQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExerciseQuestionWithPatch() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();

        // Update the exerciseQuestion using partial update
        ExerciseQuestion partialUpdatedExerciseQuestion = new ExerciseQuestion();
        partialUpdatedExerciseQuestion.setId(exerciseQuestion.getId());

        partialUpdatedExerciseQuestion
            .name(UPDATED_NAME)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExerciseQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExerciseQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExerciseQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExerciseQuestion testExerciseQuestion = exerciseQuestionList.get(exerciseQuestionList.size() - 1);
        assertThat(testExerciseQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExerciseQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testExerciseQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExerciseQuestion.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExerciseQuestion.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExerciseQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExerciseQuestion() throws Exception {
        int databaseSizeBeforeUpdate = exerciseQuestionRepository.findAll().size();
        exerciseQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExerciseQuestion in the database
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExerciseQuestion() throws Exception {
        // Initialize the database
        exerciseQuestionRepository.saveAndFlush(exerciseQuestion);

        int databaseSizeBeforeDelete = exerciseQuestionRepository.findAll().size();

        // Delete the exerciseQuestion
        restExerciseQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, exerciseQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAll();
        assertThat(exerciseQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
