package com.tertulia.booking.service;

import com.tertulia.booking.domain.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Field.
 */
public interface FieldService {

    /**
     * Save a field.
     *
     * @param field the entity to save
     * @return the persisted entity
     */
    Field save(Field field);

    /**
     *  Get all the fields.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Field> findAll(Pageable pageable);

    /**
     *  Get the "id" field.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Field findOne(Long id);

    /**
     *  Delete the "id" field.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
