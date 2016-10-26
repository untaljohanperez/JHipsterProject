package com.tertulia.booking.web.rest;

import com.tertulia.booking.BookingApp;

import com.tertulia.booking.domain.Booking;
import com.tertulia.booking.repository.BookingRepository;
import com.tertulia.booking.service.BookingService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tertulia.booking.domain.enumeration.Status;
/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApp.class)
public class BookingResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_START_STR = dateTimeFormatter.format(DEFAULT_DATE_START);

    private static final ZonedDateTime DEFAULT_DATE_ENDING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_ENDING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_ENDING_STR = dateTimeFormatter.format(DEFAULT_DATE_ENDING);

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private BookingService bookingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingResource bookingResource = new BookingResource();
        ReflectionTestUtils.setField(bookingResource, "bookingService", bookingService);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
                .dateStart(DEFAULT_DATE_START)
                .dateEnding(DEFAULT_DATE_ENDING)
                .status(DEFAULT_STATUS);
        return booking;
    }

    @Before
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testBooking.getDateEnding()).isEqualTo(DEFAULT_DATE_ENDING);
        assertThat(testBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkDateStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setDateStart(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateEndingIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setDateEnding(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setStatus(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booking)))
                .andExpect(status().isBadRequest());

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookings
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START_STR)))
                .andExpect(jsonPath("$.[*].dateEnding").value(hasItem(DEFAULT_DATE_ENDING_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START_STR))
            .andExpect(jsonPath("$.dateEnding").value(DEFAULT_DATE_ENDING_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingService.save(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findOne(booking.getId());
        updatedBooking
                .dateStart(UPDATED_DATE_START)
                .dateEnding(UPDATED_DATE_ENDING)
                .status(UPDATED_STATUS);

        restBookingMockMvc.perform(put("/api/bookings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBooking)))
                .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookings.get(bookings.size() - 1);
        assertThat(testBooking.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testBooking.getDateEnding()).isEqualTo(UPDATED_DATE_ENDING);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingService.save(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
