package com.tertulia.booking.service.impl;

import com.tertulia.booking.service.BookingService;
import com.tertulia.booking.domain.Booking;
import com.tertulia.booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing Booking.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService{

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    
    @Inject
    private BookingRepository bookingRepository;

    /**
     * Save a booking.
     *
     * @param booking the entity to save
     * @return the persisted entity
     */
    public Booking save(Booking booking) {
        log.debug("Request to save Booking : {}", booking);
        Booking result = bookingRepository.save(booking);
        return result;
    }

    /**
     *  Get all the bookings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Booking> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        Page<Booking> result = bookingRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one booking by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Booking findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        Booking booking = bookingRepository.findOne(id);
        return booking;
    }

    /**
     *  Delete the  booking by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.delete(id);
    }
    
	@Override
	public List<Booking> getBookingByDateAndField(ZonedDateTime dateEnding, ZonedDateTime dateStart, Long fieldId) {
		log.debug("getBookingByDateAndField : {} {} {}", dateStart, dateEnding, fieldId);
		return bookingRepository.findByDateStartLessThanAndDateEndingGreaterThanAndFiel_Id(dateEnding, dateStart, fieldId);
	}
}
