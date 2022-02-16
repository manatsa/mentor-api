package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExamAnswer;
import com.mana.mentor.repository.ExamAnswerRepository;
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
 * Integration tests for the {@link ExamAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamAnswerResourceIT {

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exam-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamAnswerRepository examAnswerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamAnswerMockMvc;

    private ExamAnswer examAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamAnswer createEntity(EntityManager em) {
        ExamAnswer examAnswer = new ExamAnswer()
            .answer(DEFAULT_ANSWER)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return examAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamAnswer createUpdatedEntity(EntityManager em) {
        ExamAnswer examAnswer = new ExamAnswer()
            .answer(UPDATED_ANSWER)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return examAnswer;
    }

    @BeforeEach
    public void initTest() {
        examAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createExamAnswer() throws Exception {
        int databaseSizeBeforeCreate = examAnswerRepository.findAll().size();
        // Create the ExamAnswer
        restExamAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examAnswer)))
            .andExpect(status().isCreated());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        ExamAnswer testExamAnswer = examAnswerList.get(examAnswerList.size() - 1);
        assertThat(testExamAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExamAnswer.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExamAnswer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExamAnswerWithExistingId() throws Exception {
        // Create the ExamAnswer with an existing ID
        examAnswer.setId(1L);

        int databaseSizeBeforeCreate = examAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = examAnswerRepository.findAll().size();
        // set the field null
        examAnswer.setAnswer(null);

        // Create the ExamAnswer, which fails.

        restExamAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examAnswer)))
            .andExpect(status().isBadRequest());

        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamAnswers() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        // Get all the examAnswerList
        restExamAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExamAnswer() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        // Get the examAnswer
        restExamAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, examAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExamAnswer() throws Exception {
        // Get the examAnswer
        restExamAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExamAnswer() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();

        // Update the examAnswer
        ExamAnswer updatedExamAnswer = examAnswerRepository.findById(examAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedExamAnswer are not directly saved in db
        em.detach(updatedExamAnswer);
        updatedExamAnswer.answer(UPDATED_ANSWER).dateCreated(UPDATED_DATE_CREATED).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExamAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExamAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExamAnswer testExamAnswer = examAnswerList.get(examAnswerList.size() - 1);
        assertThat(testExamAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamAnswer.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamAnswer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examAnswer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examAnswer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamAnswerWithPatch() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();

        // Update the examAnswer using partial update
        ExamAnswer partialUpdatedExamAnswer = new ExamAnswer();
        partialUpdatedExamAnswer.setId(examAnswer.getId());

        partialUpdatedExamAnswer.answer(UPDATED_ANSWER);

        restExamAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExamAnswer testExamAnswer = examAnswerList.get(examAnswerList.size() - 1);
        assertThat(testExamAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamAnswer.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExamAnswer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExamAnswerWithPatch() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();

        // Update the examAnswer using partial update
        ExamAnswer partialUpdatedExamAnswer = new ExamAnswer();
        partialUpdatedExamAnswer.setId(examAnswer.getId());

        partialUpdatedExamAnswer.answer(UPDATED_ANSWER).dateCreated(UPDATED_DATE_CREATED).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamAnswer))
            )
            .andExpect(status().isOk());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
        ExamAnswer testExamAnswer = examAnswerList.get(examAnswerList.size() - 1);
        assertThat(testExamAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExamAnswer.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamAnswer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamAnswer() throws Exception {
        int databaseSizeBeforeUpdate = examAnswerRepository.findAll().size();
        examAnswer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(examAnswer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamAnswer in the database
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamAnswer() throws Exception {
        // Initialize the database
        examAnswerRepository.saveAndFlush(examAnswer);

        int databaseSizeBeforeDelete = examAnswerRepository.findAll().size();

        // Delete the examAnswer
        restExamAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, examAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamAnswer> examAnswerList = examAnswerRepository.findAll();
        assertThat(examAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
