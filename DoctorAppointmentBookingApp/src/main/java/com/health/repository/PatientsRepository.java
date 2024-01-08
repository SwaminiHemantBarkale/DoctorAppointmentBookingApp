package com.health.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.health.entity.Doctor;
import com.health.entity.Patients;

import jakarta.transaction.Transactional;

public interface PatientsRepository extends JpaRepository<Patients, Integer> {
	
	@Query("select p from Patients p where p.username=:username")
	public Patients getUserByUserName(@Param("username") String username);
	
	@Query("select p from Patients p where p.username=:username")
	public Patients getPatientsByUsername(@Param("username") String username);
	
	@Query("select p from Patients p where p.doctor.id=:doctor_id")
	public List<Patients> getPatientsByDoctor(@Param("doctor_id") Integer doctor_id);
	
	@Query("select p from Patients p where p.doctor.id=:doctor_id and DATE(p.dAvailabilityDates.availableDateAndTimeFrom)=CURRENT_DATE")
	public List<Patients> findByTodayDate(@Param("doctor_id") Integer doctor_id);
	
	@Query("select COUNT(*) FROM Patients WHERE dAvailabilityDates.id=:id")
	int getCountByColumnName(@Param("id") int id);
	
	@Transactional
	@Modifying
	@Query(value="update Patients p set p.doctor_id= null , p.d_availability_dates_id=null where p.id=:id",nativeQuery = true)
	void deleteColumnsById(@Param("id") Integer id);
	
	
}
