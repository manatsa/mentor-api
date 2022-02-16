package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.ExamMark;
import com.mana.mentor.repository.ExamMarkRepository;
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
 * Integration tests for the {@link ExamMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamMarkResourceIT {

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/exam-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamMarkRepository examMarkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamMarkMockMvc;

    private ExamMark examMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamMark createEntity(EntityManager em) {
        ExamMark examMark = new ExamMark()
            .total(DEFAULT_TOTAL)
            .mark(DEFAULT_MARK)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return examMark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamMark createUpdatedEntity(EntityManager em) {
        ExamMark examMark = new ExamMark()
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return examMark;
    }

    @BeforeEach
    public void initTest() {
        examMark = createEntity(em);
    }

    @Test
    @Transactional
    void createExamMark() throws Exception {
        int databaseSizeBeforeCreate = examMarkRepository.findAll().size();
        // Create the ExamMark
        restExamMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examMark)))
            .andExpect(status().isCreated());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeCreate + 1);
        ExamMark testExamMark = examMarkList.get(examMarkList.size() - 1);
        assertThat(testExamMark.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testExamMark.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testExamMark.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExamMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExamMarkWithExistingId() throws Exception {
        // Create the ExamMark with an existing ID
        examMark.setId(1L);

        int databaseSizeBeforeCreate = examMarkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examMark)))
            .andExpect(status().isBadRequest());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExamMarks() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        // Get all the examMarkList
        restExamMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExamMark() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        // Get the examMark
        restExamMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, examMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examMark.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExamMark() throws Exception {
        // Get the examMark
        restExamMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExamMark() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();

        // Update the examMark
        ExamMark updatedExamMark = examMarkRepository.findById(examMark.getId()).get();
        // Disconnect from session so that the updates on updatedExamMark are not directly saved in db
        em.detach(updatedExamMark);
        updatedExamMark
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExamMark.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExamMark))
            )
            .andExpect(status().isOk());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
        ExamMark testExamMark = examMarkList.get(examMarkList.size() - 1);
        assertThat(testExamMark.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testExamMark.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testExamMark.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examMark.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examMark)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamMarkWithPatch() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();

        // Update the examMark using partial update
        ExamMark partialUpdatedExamMark = new ExamMark();
        partialUpdatedExamMark.setId(examMark.getId());

        partialUpdatedExamMark.mark(UPDATED_MARK).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamMark))
            )
            .andExpect(status().isOk());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
        ExamMark testExamMark = examMarkList.get(examMarkList.size() - 1);
        assertThat(testExamMark.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testExamMark.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testExamMark.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExamMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExamMarkWithPatch() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();

        // Update the examMark using partial update
        ExamMark partialUpdatedExamMark = new ExamMark();
        partialUpdatedExamMark.setId(examMark.getId());

        partialUpdatedExamMark
            .total(UPDATED_TOTAL)
            .mark(UPDATED_MARK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExamMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamMark))
            )
            .andExpect(status().isOk());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
        ExamMark testExamMark = examMarkList.get(examMarkList.size() - 1);
        assertThat(testExamMark.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testExamMark.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testExamMark.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExamMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examMark))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamMark() throws Exception {
        int databaseSizeBeforeUpdate = examMarkRepository.findAll().size();
        examMark.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(examMark)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamMark in the database
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamMark() throws Exception {
        // Initialize the database
        examMarkRepository.saveAndFlush(examMark);

        int databaseSizeBeforeDelete = examMarkRepository.findAll().size();

        // Delete the examMark
        restExamMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, examMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamMark> examMarkList = examMarkRepository.findAll();
        assertThat(examMarkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
