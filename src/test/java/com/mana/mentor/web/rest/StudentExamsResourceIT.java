package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.StudentExams;
import com.mana.mentor.repository.StudentExamsRepository;
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
 * Integration tests for the {@link StudentExamsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentExamsResourceIT {

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Instant DEFAULT_FINISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/student-exams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentExamsRepository studentExamsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentExamsMockMvc;

    private StudentExams studentExams;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentExams createEntity(EntityManager em) {
        StudentExams studentExams = new StudentExams()
            .completed(DEFAULT_COMPLETED)
            .finishDate(DEFAULT_FINISH_DATE)
            .mark(DEFAULT_MARK)
            .total(DEFAULT_TOTAL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return studentExams;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentExams createUpdatedEntity(EntityManager em) {
        StudentExams studentExams = new StudentExams()
            .completed(UPDATED_COMPLETED)
            .finishDate(UPDATED_FINISH_DATE)
            .mark(UPDATED_MARK)
            .total(UPDATED_TOTAL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return studentExams;
    }

    @BeforeEach
    public void initTest() {
        studentExams = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentExams() throws Exception {
        int databaseSizeBeforeCreate = studentExamsRepository.findAll().size();
        // Create the StudentExams
        restStudentExamsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentExams)))
            .andExpect(status().isCreated());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentExams testStudentExams = studentExamsList.get(studentExamsList.size() - 1);
        assertThat(testStudentExams.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testStudentExams.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
        assertThat(testStudentExams.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testStudentExams.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testStudentExams.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testStudentExams.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createStudentExamsWithExistingId() throws Exception {
        // Create the StudentExams with an existing ID
        studentExams.setId(1L);

        int databaseSizeBeforeCreate = studentExamsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentExamsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentExams)))
            .andExpect(status().isBadRequest());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentExamsRepository.findAll().size();
        // set the field null
        studentExams.setCompleted(null);

        // Create the StudentExams, which fails.

        restStudentExamsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentExams)))
            .andExpect(status().isBadRequest());

        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentExamsRepository.findAll().size();
        // set the field null
        studentExams.setFinishDate(null);

        // Create the StudentExams, which fails.

        restStudentExamsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentExams)))
            .andExpect(status().isBadRequest());

        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentExams() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        // Get all the studentExamsList
        restStudentExamsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentExams.getId().intValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStudentExams() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        // Get the studentExams
        restStudentExamsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentExams.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentExams.getId().intValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStudentExams() throws Exception {
        // Get the studentExams
        restStudentExamsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentExams() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();

        // Update the studentExams
        StudentExams updatedStudentExams = studentExamsRepository.findById(studentExams.getId()).get();
        // Disconnect from session so that the updates on updatedStudentExams are not directly saved in db
        em.detach(updatedStudentExams);
        updatedStudentExams
            .completed(UPDATED_COMPLETED)
            .finishDate(UPDATED_FINISH_DATE)
            .mark(UPDATED_MARK)
            .total(UPDATED_TOTAL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStudentExamsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentExams.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentExams))
            )
            .andExpect(status().isOk());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
        StudentExams testStudentExams = studentExamsList.get(studentExamsList.size() - 1);
        assertThat(testStudentExams.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testStudentExams.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
        assertThat(testStudentExams.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testStudentExams.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testStudentExams.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testStudentExams.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentExams.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentExams))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentExams))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentExams)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentExamsWithPatch() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();

        // Update the studentExams using partial update
        StudentExams partialUpdatedStudentExams = new StudentExams();
        partialUpdatedStudentExams.setId(studentExams.getId());

        partialUpdatedStudentExams.mark(UPDATED_MARK).dateCreated(UPDATED_DATE_CREATED).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStudentExamsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentExams.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentExams))
            )
            .andExpect(status().isOk());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
        StudentExams testStudentExams = studentExamsList.get(studentExamsList.size() - 1);
        assertThat(testStudentExams.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testStudentExams.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
        assertThat(testStudentExams.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testStudentExams.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testStudentExams.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testStudentExams.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateStudentExamsWithPatch() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();

        // Update the studentExams using partial update
        StudentExams partialUpdatedStudentExams = new StudentExams();
        partialUpdatedStudentExams.setId(studentExams.getId());

        partialUpdatedStudentExams
            .completed(UPDATED_COMPLETED)
            .finishDate(UPDATED_FINISH_DATE)
            .mark(UPDATED_MARK)
            .total(UPDATED_TOTAL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restStudentExamsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentExams.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentExams))
            )
            .andExpect(status().isOk());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
        StudentExams testStudentExams = studentExamsList.get(studentExamsList.size() - 1);
        assertThat(testStudentExams.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testStudentExams.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
        assertThat(testStudentExams.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testStudentExams.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testStudentExams.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testStudentExams.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentExams.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentExams))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentExams))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentExams() throws Exception {
        int databaseSizeBeforeUpdate = studentExamsRepository.findAll().size();
        studentExams.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentExamsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentExams))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentExams in the database
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentExams() throws Exception {
        // Initialize the database
        studentExamsRepository.saveAndFlush(studentExams);

        int databaseSizeBeforeDelete = studentExamsRepository.findAll().size();

        // Delete the studentExams
        restStudentExamsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentExams.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentExams> studentExamsList = studentExamsRepository.findAll();
        assertThat(studentExamsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
