package com.tertulia.booking.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.tertulia.booking.domain.enumeration.Status;

/**
 * not an ignored comment                                                      
 * 
 */
@ApiModel(description = ""
    + "not an ignored comment                                                 "
    + "")
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private ZonedDateTime dateStart;

    @NotNull
    @Column(name = "date_ending", nullable = false)
    private ZonedDateTime dateEnding;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Field fiel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateStart() {
        return dateStart;
    }

    public Booking dateStart(ZonedDateTime dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public void setDateStart(ZonedDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public ZonedDateTime getDateEnding() {
        return dateEnding;
    }

    public Booking dateEnding(ZonedDateTime dateEnding) {
        this.dateEnding = dateEnding;
        return this;
    }

    public void setDateEnding(ZonedDateTime dateEnding) {
        this.dateEnding = dateEnding;
    }

    public Status getStatus() {
        return status;
    }

    public Booking status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Booking customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Field getFiel() {
        return fiel;
    }

    public Booking fiel(Field field) {
        this.fiel = field;
        return this;
    }

    public void setFiel(Field field) {
        this.fiel = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        if(booking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + id +
            ", dateStart='" + dateStart + "'" +
            ", dateEnding='" + dateEnding + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
