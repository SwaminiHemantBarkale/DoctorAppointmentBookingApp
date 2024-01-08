package com.health.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class DoctorAvailabilityDates {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column
	//@DateTimeFormat
	private String availableDateAndTimeFrom;
	
	@Column
	//@DateTimeFormat
	private String availableDateAndTimeTo;
	
	
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy="dAvailabilityDates")
	//private List<DoctorAvailabilityTimeForSpecificDate> docAvailabilityTimeForSpecificDates= new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy="dAvailabilityDates")
	private List<Patients> patients=new ArrayList<>();
	
	@ManyToOne
	private Doctor doctor;

}
