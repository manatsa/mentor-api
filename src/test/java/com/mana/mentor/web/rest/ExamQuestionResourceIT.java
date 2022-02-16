package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExamQuestion;
import com.mana.mentor.domain.enumeration.Level;
import com.mana.mentor.domain.enumeration.TestType;
import com.mana.mentor.repository.ExamQuestionRepository;
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
 * Integration tests for the {@link ExamQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamQuestionResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final TestType DEFAULT_TYPE = TestType.TEST;
    private static final TestType UPDATED_TYPE = TestType.EXAM;

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final Level DEFAULT_LEVEL = Level.GRADE_1;
    private static final Level UPDATED_LEVEL = Level.GRADE_2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exam-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamQuestionMockMvc;

    private ExamQuestion examQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQuestion createEntity(EntityManager em) {
        ExamQuestion examQuestion = new ExamQuestion()
            .question(DEFAULT_QUESTION)
            .type(DEFAULT_TYPE)
            .answer(DEFAULT_ANSWER)
            .explanation(DEFAULT_EXPLANATION)
            .level(DEFAULT_LEVEL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return examQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQuestion createUpdatedEntity(EntityManager em) {
        ExamQuestion examQuestion = new ExamQuestion()
            .question(UPDATED_QUESTION)
            .type(UPDATED_TYPE)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return examQuestion;
    }

    @BeforeEach
    public void initTest() {
        examQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createExamQuestion() throws Exception {
        int databaseSizeBeforeCreate = examQuestionRepository.findAll().size();
        // Create the ExamQuestion
        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isCreated());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        ExamQuestion testExamQuestion = examQuestionList.get(examQuestionList.size() - 1);
        assertThat(testExamQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testExamQuestion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExamQuestion.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExamQuestion.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testExamQuestion.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testExamQuestion.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExamQuestion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExamQuestionWithExistingId() throws Exception {
        // Create the ExamQuestion with an existing ID
        examQuestion.setId(1L);

        int databaseSizeBeforeCreate = examQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuestionRepository.findAll().size();
        // set the field null
        examQuestion.setQuestion(null);

        // Create the ExamQuestion, which fails.

        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isBadRequest());

        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuestionRepository.findAll().size();
        // set the field null
        examQuestion.setType(null);

        // Create the ExamQuestion, which fails.

        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isBadRequest());

        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuestionRepository.findAll().size();
        // set the field null
        examQuestion.setAnswer(null);

        // Create the ExamQuestion, which fails.

        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isBadRequest());

        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuestionRepository.findAll().size();
        // set the field null
        examQuestion.setLevel(null);

        // Create the ExamQuestion, which fails.

        restExamQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isBadRequest());

        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamQuestions() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        // Get all the examQuestionList
        restExamQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExamQuestion() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        // Get the examQuestion
        restExamQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, examQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examQuestion.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExamQuestion() throws Exception {
        // Get the examQuestion
        restExamQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExamQuestion() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();

        // Update the examQuestion
        ExamQuestion updatedExamQuestion = examQuestionRepository.findById(examQuestion.getId()).get();
        // Disconnect from session so that the updates on updatedExamQuestion are not directly saved in db
        em.detach(updatedExamQuestion);
        updatedExamQuestion
            .question(UPDATED_QUESTION)
            .type(UPDATED_TYPE)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExamQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExamQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExamQuestion testExamQuestion = examQuestionList.get(examQuestionList.size() - 1);
        assertThat(testExamQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testExamQuestion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExamQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamQuestion.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExamQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testExamQuestion.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examQuestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamQuestionWithPatch() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();

        // Update the examQuestion using partial update
        ExamQuestion partialUpdatedExamQuestion = new ExamQuestion();
        partialUpdatedExamQuestion.setId(examQuestion.getId());

        partialUpdatedExamQuestion
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED);

        restExamQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExamQuestion testExamQuestion = examQuestionList.get(examQuestionList.size() - 1);
        assertThat(testExamQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testExamQuestion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testExamQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamQuestion.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExamQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testExamQuestion.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamQuestion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExamQuestionWithPatch() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();

        // Update the examQuestion using partial update
        ExamQuestion partialUpdatedExamQuestion = new ExamQuestion();
        partialUpdatedExamQuestion.setId(examQuestion.getId());

        partialUpdatedExamQuestion
            .question(UPDATED_QUESTION)
            .type(UPDATED_TYPE)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .level(UPDATED_LEVEL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
        ExamQuestion testExamQuestion = examQuestionList.get(examQuestionList.size() - 1);
        assertThat(testExamQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testExamQuestion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExamQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamQuestion.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testExamQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testExamQuestion.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamQuestion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = examQuestionRepository.findAll().size();
        examQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(examQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamQuestion in the database
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamQuestion() throws Exception {
        // Initialize the database
        examQuestionRepository.saveAndFlush(examQuestion);

        int databaseSizeBeforeDelete = examQuestionRepository.findAll().size();

        // Delete the examQuestion
        restExamQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, examQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamQuestion> examQuestionList = examQuestionRepository.findAll();
        assertThat(examQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
