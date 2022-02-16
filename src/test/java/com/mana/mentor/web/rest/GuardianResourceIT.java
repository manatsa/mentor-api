package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.Guardian;
import com.mana.mentor.repository.GuardianRepository;
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
 * Integration tests for the {@link GuardianResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GuardianResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/guardians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuardianMockMvc;

    private Guardian guardian;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardian createEntity(EntityManager em) {
        Guardian guardian = new Guardian()
            .title(DEFAULT_TITLE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .idNumber(DEFAULT_ID_NUMBER)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return guardian;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardian createUpdatedEntity(EntityManager em) {
        Guardian guardian = new Guardian()
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return guardian;
    }

    @BeforeEach
    public void initTest() {
        guardian = createEntity(em);
    }

    @Test
    @Transactional
    void createGuardian() throws Exception {
        int databaseSizeBeforeCreate = guardianRepository.findAll().size();
        // Create the Guardian
        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isCreated());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeCreate + 1);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGuardian.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testGuardian.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testGuardian.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testGuardian.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testGuardian.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGuardian.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testGuardian.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createGuardianWithExistingId() throws Exception {
        // Create the Guardian with an existing ID
        guardian.setId(1L);

        int databaseSizeBeforeCreate = guardianRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setTitle(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setFirstName(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setLastName(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setIdNumber(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setAddress(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setPhone(null);

        // Create the Guardian, which fails.

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGuardians() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        // Get all the guardianList
        restGuardianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardian.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        // Get the guardian
        restGuardianMockMvc
            .perform(get(ENTITY_API_URL_ID, guardian.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guardian.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGuardian() throws Exception {
        // Get the guardian
        restGuardianMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian
        Guardian updatedGuardian = guardianRepository.findById(guardian.getId()).get();
        // Disconnect from session so that the updates on updatedGuardian are not directly saved in db
        em.detach(updatedGuardian);
        updatedGuardian
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGuardian.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGuardian))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGuardian.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testGuardian.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testGuardian.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testGuardian.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testGuardian.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGuardian.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testGuardian.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardian.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardian))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardian))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuardianWithPatch() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian using partial update
        Guardian partialUpdatedGuardian = new Guardian();
        partialUpdatedGuardian.setId(guardian.getId());

        partialUpdatedGuardian
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .phone(UPDATED_PHONE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardian))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testGuardian.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testGuardian.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testGuardian.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testGuardian.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testGuardian.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGuardian.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testGuardian.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGuardianWithPatch() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian using partial update
        Guardian partialUpdatedGuardian = new Guardian();
        partialUpdatedGuardian.setId(guardian.getId());

        partialUpdatedGuardian
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardian))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testGuardian.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testGuardian.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testGuardian.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testGuardian.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testGuardian.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGuardian.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testGuardian.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardian))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardian))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(guardian)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeDelete = guardianRepository.findAll().size();

        // Delete the guardian
        restGuardianMockMvc
            .perform(delete(ENTITY_API_URL_ID, guardian.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
