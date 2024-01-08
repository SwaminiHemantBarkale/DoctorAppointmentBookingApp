
package com.health.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class MyConfig  {
	
	@Bean
	public UserDetailsService getDoctorDetailsService() {
		
		return new DoctorDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(getDoctorDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}	
	

	@SuppressWarnings({ "removal", "deprecation" })
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.authenticationProvider(authenticationProvider());
		
		http.authorizeRequests()
		.requestMatchers("/doctor/**").hasAnyRole("DOCTOR")
		.requestMatchers("/**").permitAll().and().formLogin().loginPage("/login1").and().csrf().disable();
		
		return http.build();
	}
	
	
	@Bean
	public UserDetailsService getPatientsDetailsService() {
		
		return new PatientsDetailsServiceImpl();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider2() {
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(getPatientsDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}	
	

	@SuppressWarnings({ "removal", "deprecation" })
	@Bean
	public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception{
		
		http.authenticationProvider(authenticationProvider());
		
		http.authorizeRequests()
		.requestMatchers("/**").hasAnyRole("PATIENTS")
		.requestMatchers("/**").permitAll().and().formLogin().loginPage("/login2").and().csrf().disable();
		
		return http.build();
	}
    
   
}
