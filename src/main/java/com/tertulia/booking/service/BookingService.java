package com.tertulia.booking.service;

import com.tertulia.booking.domain.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Interface for managing Booking.
 */
public interface BookingService {

    /**
     * Save a booking.
     *
     * @param booking the entity to save
     * @return the persisted entity
     */
    Booking save(Booking booking);

    /**
     *  Get all the bookings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Booking> findAll(Pageable pageable);

    /**
     *  Get the "id" booking.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Booking findOne(Long id);

    /**
     *  Delete the "id" booking.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    List<Booking> getBookingByDateAndField(ZonedDateTime dateEnding, ZonedDateTime dateStart, Long fieldId);
}
