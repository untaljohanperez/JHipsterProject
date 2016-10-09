package com.tertulia.booking.web.rest;

import com.tertulia.booking.BookingApp;

import com.tertulia.booking.domain.Field;
import com.tertulia.booking.repository.FieldRepository;
import com.tertulia.booking.service.FieldService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tertulia.booking.domain.enumeration.Sport;
/**
 * Test class for the FieldResource REST controller.
 *
 * @see FieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApp.class)
public class FieldResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final Sport DEFAULT_SPORT = Sport.SOCCER;
    private static final Sport UPDATED_SPORT = Sport.SWIMMING;

    @Inject
    private FieldRepository fieldRepository;

    @Inject
    private FieldService fieldService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFieldMockMvc;

    private Field field;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FieldResource fieldResource = new FieldResource();
        ReflectionTestUtils.setField(fieldResource, "fieldService", fieldService);
        this.restFieldMockMvc = MockMvcBuilders.standaloneSetup(fieldResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createEntity(EntityManager em) {
        Field field = new Field()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS)
                .sport(DEFAULT_SPORT);
        return field;
    }

    @Before
    public void initTest() {
        field = createEntity(em);
    }

    @Test
    @Transactional
    public void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // Create the Field

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field)))
                .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fields.get(fields.size() - 1);
        assertThat(testField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testField.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testField.getSport()).isEqualTo(DEFAULT_SPORT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setName(null);

        // Create the Field, which fails.

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field)))
                .andExpect(status().isBadRequest());

        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setAddress(null);

        // Create the Field, which fails.

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field)))
                .andExpect(status().isBadRequest());

        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSportIsRequired() throws Exception {
        int databaseSizeBeforeTest = fieldRepository.findAll().size();
        // set the field null
        field.setSport(null);

        // Create the Field, which fails.

        restFieldMockMvc.perform(post("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(field)))
                .andExpect(status().isBadRequest());

        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fields
        restFieldMockMvc.perform(get("/api/fields?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].sport").value(hasItem(DEFAULT_SPORT.toString())));
    }

    @Test
    @Transactional
    public void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.sport").value(DEFAULT_SPORT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get("/api/fields/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findOne(field.getId());
        updatedField
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS)
                .sport(UPDATED_SPORT);

        restFieldMockMvc.perform(put("/api/fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedField)))
                .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeUpdate);
        Field testField = fields.get(fields.size() - 1);
        assertThat(testField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testField.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testField.getSport()).isEqualTo(UPDATED_SPORT);
    }

    @Test
    @Transactional
    public void deleteField() throws Exception {
        // Initialize the database
        fieldService.save(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Get the field
        restFieldMockMvc.perform(delete("/api/fields/{id}", field.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Field> fields = fieldRepository.findAll();
        assertThat(fields).hasSize(databaseSizeBeforeDelete - 1);
    }
}
