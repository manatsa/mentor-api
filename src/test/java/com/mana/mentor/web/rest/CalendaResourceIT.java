package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.Calenda;
import com.mana.mentor.domain.enumeration.NewsEventCategory;
import com.mana.mentor.repository.CalendaRepository;
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
 * Integration tests for the {@link CalendaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendaResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final NewsEventCategory DEFAULT_CATEGORY = NewsEventCategory.SOCIAL;
    private static final NewsEventCategory UPDATED_CATEGORY = NewsEventCategory.EDUCATIONAL;

    private static final Instant DEFAULT_EVENT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EVENT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/calendas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendaRepository calendaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendaMockMvc;

    private Calenda calenda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calenda createEntity(EntityManager em) {
        Calenda calenda = new Calenda()
            .eventName(DEFAULT_EVENT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .eventStartDate(DEFAULT_EVENT_START_DATE)
            .eventEndDate(DEFAULT_EVENT_END_DATE)
            .location(DEFAULT_LOCATION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return calenda;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calenda createUpdatedEntity(EntityManager em) {
        Calenda calenda = new Calenda()
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .eventStartDate(UPDATED_EVENT_START_DATE)
            .eventEndDate(UPDATED_EVENT_END_DATE)
            .location(UPDATED_LOCATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return calenda;
    }

    @BeforeEach
    public void initTest() {
        calenda = createEntity(em);
    }

    @Test
    @Transactional
    void createCalenda() throws Exception {
        int databaseSizeBeforeCreate = calendaRepository.findAll().size();
        // Create the Calenda
        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isCreated());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeCreate + 1);
        Calenda testCalenda = calendaList.get(calendaList.size() - 1);
        assertThat(testCalenda.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testCalenda.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalenda.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCalenda.getEventStartDate()).isEqualTo(DEFAULT_EVENT_START_DATE);
        assertThat(testCalenda.getEventEndDate()).isEqualTo(DEFAULT_EVENT_END_DATE);
        assertThat(testCalenda.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCalenda.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testCalenda.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCalendaWithExistingId() throws Exception {
        // Create the Calenda with an existing ID
        calenda.setId(1L);

        int databaseSizeBeforeCreate = calendaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendaRepository.findAll().size();
        // set the field null
        calenda.setEventName(null);

        // Create the Calenda, which fails.

        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendaRepository.findAll().size();
        // set the field null
        calenda.setCategory(null);

        // Create the Calenda, which fails.

        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendaRepository.findAll().size();
        // set the field null
        calenda.setEventStartDate(null);

        // Create the Calenda, which fails.

        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendaRepository.findAll().size();
        // set the field null
        calenda.setEventEndDate(null);

        // Create the Calenda, which fails.

        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendaRepository.findAll().size();
        // set the field null
        calenda.setLocation(null);

        // Create the Calenda, which fails.

        restCalendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isBadRequest());

        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalendas() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        // Get all the calendaList
        restCalendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].eventStartDate").value(hasItem(DEFAULT_EVENT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventEndDate").value(hasItem(DEFAULT_EVENT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCalenda() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        // Get the calenda
        restCalendaMockMvc
            .perform(get(ENTITY_API_URL_ID, calenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calenda.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.eventStartDate").value(DEFAULT_EVENT_START_DATE.toString()))
            .andExpect(jsonPath("$.eventEndDate").value(DEFAULT_EVENT_END_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCalenda() throws Exception {
        // Get the calenda
        restCalendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalenda() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();

        // Update the calenda
        Calenda updatedCalenda = calendaRepository.findById(calenda.getId()).get();
        // Disconnect from session so that the updates on updatedCalenda are not directly saved in db
        em.detach(updatedCalenda);
        updatedCalenda
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .eventStartDate(UPDATED_EVENT_START_DATE)
            .eventEndDate(UPDATED_EVENT_END_DATE)
            .location(UPDATED_LOCATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCalendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalenda.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalenda))
            )
            .andExpect(status().isOk());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
        Calenda testCalenda = calendaList.get(calendaList.size() - 1);
        assertThat(testCalenda.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testCalenda.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalenda.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCalenda.getEventStartDate()).isEqualTo(UPDATED_EVENT_START_DATE);
        assertThat(testCalenda.getEventEndDate()).isEqualTo(UPDATED_EVENT_END_DATE);
        assertThat(testCalenda.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCalenda.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testCalenda.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calenda.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendaWithPatch() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();

        // Update the calenda using partial update
        Calenda partialUpdatedCalenda = new Calenda();
        partialUpdatedCalenda.setId(calenda.getId());

        partialUpdatedCalenda
            .eventName(UPDATED_EVENT_NAME)
            .category(UPDATED_CATEGORY)
            .eventEndDate(UPDATED_EVENT_END_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCalendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalenda))
            )
            .andExpect(status().isOk());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
        Calenda testCalenda = calendaList.get(calendaList.size() - 1);
        assertThat(testCalenda.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testCalenda.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCalenda.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCalenda.getEventStartDate()).isEqualTo(DEFAULT_EVENT_START_DATE);
        assertThat(testCalenda.getEventEndDate()).isEqualTo(UPDATED_EVENT_END_DATE);
        assertThat(testCalenda.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCalenda.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testCalenda.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCalendaWithPatch() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();

        // Update the calenda using partial update
        Calenda partialUpdatedCalenda = new Calenda();
        partialUpdatedCalenda.setId(calenda.getId());

        partialUpdatedCalenda
            .eventName(UPDATED_EVENT_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .eventStartDate(UPDATED_EVENT_START_DATE)
            .eventEndDate(UPDATED_EVENT_END_DATE)
            .location(UPDATED_LOCATION)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCalendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalenda))
            )
            .andExpect(status().isOk());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
        Calenda testCalenda = calendaList.get(calendaList.size() - 1);
        assertThat(testCalenda.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testCalenda.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCalenda.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCalenda.getEventStartDate()).isEqualTo(UPDATED_EVENT_START_DATE);
        assertThat(testCalenda.getEventEndDate()).isEqualTo(UPDATED_EVENT_END_DATE);
        assertThat(testCalenda.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCalenda.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testCalenda.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalenda() throws Exception {
        int databaseSizeBeforeUpdate = calendaRepository.findAll().size();
        calenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calenda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calenda in the database
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalenda() throws Exception {
        // Initialize the database
        calendaRepository.saveAndFlush(calenda);

        int databaseSizeBeforeDelete = calendaRepository.findAll().size();

        // Delete the calenda
        restCalendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, calenda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calenda> calendaList = calendaRepository.findAll();
        assertThat(calendaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
