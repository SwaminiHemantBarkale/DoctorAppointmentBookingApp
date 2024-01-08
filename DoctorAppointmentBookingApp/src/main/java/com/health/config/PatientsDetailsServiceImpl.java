package com.health.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.health.entity.Patients;
import com.health.repository.PatientsRepository;

@Service
public class PatientsDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private PatientsRepository patientsRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Patients patients=patientsRepository.getUserByUserName(username);
		
		if(patients==null) {
			throw new UsernameNotFoundException("Username not found !!!");
		}
		
		CustomPatientsDetails customPatientsDetails=new CustomPatientsDetails(patients);
		return customPatientsDetails;
	}
	
	

}
