package com.health.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.health.entity.DoctorAvailabilityDates;

public interface DoctorAvailabilityDatesRepository extends JpaRepository<DoctorAvailabilityDates, Integer> {
	
	@Query("select a from DoctorAvailabilityDates a where a.doctor.id=:doctor_id and DATE(a.availableDateAndTimeFrom)= CURRENT_DATE or DATE(a.availableDateAndTimeFrom)> CURRENT_DATE")
	public List<DoctorAvailabilityDates> getDoctorAvailabilityDatesByDoctor(@Param("doctor_id") int doctor_id);

	
	@Query("select a from DoctorAvailabilityDates a where DATE(a.availableDateAndTimeFrom)= CURRENT_DATE ")
	public List<DoctorAvailabilityDates> findByTodayDate();
}
