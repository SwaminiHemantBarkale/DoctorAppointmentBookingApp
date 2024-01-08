package com.health.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.health.entity.Doctor;
import com.health.repository.DoctorRepository;

@Service
public class DoctorDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private DoctorRepository doctorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Doctor doctor=doctorRepository.getUserByUserName(username);
		
		 if(doctor==null) {
			 throw new UsernameNotFoundException("Doctor not found !!!!");
		 }
		 
		 CustomDoctorDetails customUserDetails=new CustomDoctorDetails(doctor);
		 return customUserDetails;
		
	}
	
}
