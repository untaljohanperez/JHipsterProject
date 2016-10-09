package com.tertulia.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tertulia.booking.domain.Booking;
import com.tertulia.booking.service.BookingService;
import com.tertulia.booking.web.rest.util.HeaderUtil;
import com.tertulia.booking.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Booking.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);
        
    @Inject
    private BookingService bookingService;

    /**
     * POST  /bookings : Create a new booking.
     *
     * @param booking the booking to create
     * @return the ResponseEntity with status 201 (Created) and with body the new booking, or with status 400 (Bad Request) if the booking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("booking", "idexists", "A new booking cannot already have an ID")).body(null);
        }
        Booking result = bookingService.save(booking);
        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("booking", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bookings : Updates an existing booking.
     *
     * @param booking the booking to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated booking,
     * or with status 400 (Bad Request) if the booking is not valid,
     * or with status 500 (Internal Server Error) if the booking couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", booking);
        if (booking.getId() == null) {
            return createBooking(booking);
        }
        Booking result = bookingService.save(booking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("booking", booking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookings : get all the bookings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bookings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Booking>> getAllBookings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bookings");
        Page<Booking> page = bookingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bookings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bookings/:id : get the "id" booking.
     *
     * @param id the id of the booking to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the booking, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bookings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Booking booking = bookingService.findOne(id);
        return Optional.ofNullable(booking)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookings/:id : delete the "id" booking.
     *
     * @param id the id of the booking to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bookings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("booking", id.toString())).build();
    }

}
