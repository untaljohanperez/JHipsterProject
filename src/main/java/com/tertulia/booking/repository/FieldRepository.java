package com.tertulia.booking.repository;

import com.tertulia.booking.domain.Field;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Field entity.
 */
@SuppressWarnings("unused")
public interface FieldRepository extends JpaRepository<Field,Long> {

}
