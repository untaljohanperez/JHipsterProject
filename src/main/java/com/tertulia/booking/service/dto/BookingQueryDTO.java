package com.tertulia.booking.service.dto;

import java.time.ZonedDateTime;

public class BookingQueryDTO {
	
	private ZonedDateTime dateStart;
	private ZonedDateTime dateEnding;
	private Long fieldId;
	private String status;
	public ZonedDateTime getDateStart() {
		return dateStart;
	}
	public void setDateStart(ZonedDateTime dateStart) {
		this.dateStart = dateStart;
	}
	public ZonedDateTime getDateEnding() {
		return dateEnding;
	}	
	public void setDateEnding(ZonedDateTime dateEnding) {
		this.dateEnding = dateEnding;
	}
	public Long getFieldId() {
		return fieldId;
	}
	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BookingQueryDTO(ZonedDateTime dateStart, ZonedDateTime dateEnding, Long fieldId, String status) {
		super();
		this.dateStart = dateStart;
		this.dateEnding = dateEnding;
		this.fieldId = fieldId;
		this.status = status;
	}
	
	public BookingQueryDTO() {
	}

}
