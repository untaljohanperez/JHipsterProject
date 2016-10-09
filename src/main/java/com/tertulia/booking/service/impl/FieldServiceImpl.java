package com.tertulia.booking.service.impl;

import com.tertulia.booking.service.FieldService;
import com.tertulia.booking.domain.Field;
import com.tertulia.booking.repository.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Field.
 */
@Service
@Transactional
public class FieldServiceImpl implements FieldService{

    private final Logger log = LoggerFactory.getLogger(FieldServiceImpl.class);
    
    @Inject
    private FieldRepository fieldRepository;

    /**
     * Save a field.
     *
     * @param field the entity to save
     * @return the persisted entity
     */
    public Field save(Field field) {
        log.debug("Request to save Field : {}", field);
        Field result = fieldRepository.save(field);
        return result;
    }

    /**
     *  Get all the fields.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Field> findAll(Pageable pageable) {
        log.debug("Request to get all Fields");
        Page<Field> result = fieldRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one field by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Field findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        Field field = fieldRepository.findOne(id);
        return field;
    }

    /**
     *  Delete the  field by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.delete(id);
    }
}
