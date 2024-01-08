package com.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.health.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
	
	@Query("select d from Doctor d where d.username=:username")
	public Doctor getUserByUserName(@Param("username") String username);
	
	@Query("select d from Doctor d where d.username=:username")
	public Doctor getDoctorByUsername(@Param("username") String username);
	
	@Query("select d from Doctor d where d.specialization=:specialization")
	public Doctor getDoctorBySpecialization(@Param("specialization") String specialization);

	@Query("select name from Doctor d where d.username=:username")
	public String getName(@Param("username") String username);
}
