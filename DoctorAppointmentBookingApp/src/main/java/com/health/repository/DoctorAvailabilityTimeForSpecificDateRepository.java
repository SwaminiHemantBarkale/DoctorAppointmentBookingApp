package com.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.health.entity.DoctorAvailabilityTimeForSpecificDate;

public interface DoctorAvailabilityTimeForSpecificDateRepository extends JpaRepository<DoctorAvailabilityTimeForSpecificDate, Integer> {

}
