package com.mana.mentor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mana.mentor.IntegrationTest;
import com.mana.mentor.domain.Example;
import com.mana.mentor.repository.ExampleRepository;
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
 * Integration tests for the {@link ExampleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExampleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/examples";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExampleMockMvc;

    private Example example;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Example createEntity(EntityManager em) {
        Example example = new Example()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return example;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Example createUpdatedEntity(EntityManager em) {
        Example example = new Example()
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return example;
    }

    @BeforeEach
    public void initTest() {
        example = createEntity(em);
    }

    @Test
    @Transactional
    void createExample() throws Exception {
        int databaseSizeBeforeCreate = exampleRepository.findAll().size();
        // Create the Example
        restExampleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(example)))
            .andExpect(status().isCreated());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeCreate + 1);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExample.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testExample.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testExample.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testExample.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testExample.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createExampleWithExistingId() throws Exception {
        // Create the Example with an existing ID
        example.setId(1L);

        int databaseSizeBeforeCreate = exampleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExampleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(example)))
            .andExpect(status().isBadRequest());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exampleRepository.findAll().size();
        // set the field null
        example.setName(null);

        // Create the Example, which fails.

        restExampleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(example)))
            .andExpect(status().isBadRequest());

        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamples() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        // Get all the exampleList
        restExampleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(example.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExample() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        // Get the example
        restExampleMockMvc
            .perform(get(ENTITY_API_URL_ID, example.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(example.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExample() throws Exception {
        // Get the example
        restExampleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExample() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();

        // Update the example
        Example updatedExample = exampleRepository.findById(example.getId()).get();
        // Disconnect from session so that the updates on updatedExample are not directly saved in db
        em.detach(updatedExample);
        updatedExample
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExample.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExample))
            )
            .andExpect(status().isOk());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExample.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExample.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testExample.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testExample.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExample.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, example.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(example))
            )
            .andExpect(status().isBadRequest());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(example))
            )
            .andExpect(status().isBadRequest());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(example)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExampleWithPatch() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();

        // Update the example using partial update
        Example partialUpdatedExample = new Example();
        partialUpdatedExample.setId(example.getId());

        partialUpdatedExample
            .content(UPDATED_CONTENT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExample.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExample))
            )
            .andExpect(status().isOk());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExample.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExample.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testExample.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testExample.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExample.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateExampleWithPatch() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();

        // Update the example using partial update
        Example partialUpdatedExample = new Example();
        partialUpdatedExample.setId(example.getId());

        partialUpdatedExample
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExample.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExample))
            )
            .andExpect(status().isOk());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
        Example testExample = exampleList.get(exampleList.size() - 1);
        assertThat(testExample.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExample.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testExample.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testExample.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testExample.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testExample.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, example.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(example))
            )
            .andExpect(status().isBadRequest());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(example))
            )
            .andExpect(status().isBadRequest());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExample() throws Exception {
        int databaseSizeBeforeUpdate = exampleRepository.findAll().size();
        example.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(example)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Example in the database
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExample() throws Exception {
        // Initialize the database
        exampleRepository.saveAndFlush(example);

        int databaseSizeBeforeDelete = exampleRepository.findAll().size();

        // Delete the example
        restExampleMockMvc
            .perform(delete(ENTITY_API_URL_ID, example.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Example> exampleList = exampleRepository.findAll();
        assertThat(exampleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
