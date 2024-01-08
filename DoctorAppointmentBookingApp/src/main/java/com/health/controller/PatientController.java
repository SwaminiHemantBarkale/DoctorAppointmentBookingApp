package com.health.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.health.entity.Doctor;
import com.health.entity.DoctorAvailabilityDates;
import com.health.entity.Patients;
import com.health.helper.Message;
import com.health.repository.DoctorAvailabilityDatesRepository;
import com.health.repository.DoctorRepository;
import com.health.repository.PatientsRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PatientController {
	
	@Autowired
	private PatientsRepository patientsRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private DoctorAvailabilityDatesRepository doctorAvailabilityDatesRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/patientRegistration")
	public String patientRegistration() {
		
		return "patientRegistrationForm";
	}
	
	@PostMapping("/processPatientRegistrationForm")
	public String processPatientRegistrationForm(@ModelAttribute Patients patients,
			                                     @RequestParam("name") String name,
			                                     @RequestParam("username") String username,
			                                     @RequestParam("password") String password,
			                                     HttpSession session) {
		
	try {	
		patients.setName(name);
		patients.setUsername(username);
		patients.setPassword(bCryptPasswordEncoder.encode(patients.getPassword()));
		patients.setRole("ROLE_PATIENTS");
		
		patientsRepository.save(patients);
		
		session.setAttribute("message", new Message("You registered successfully !!! You may login now", "alert-success"));
		return "index";
	}
	
	catch(Exception e) {
		
		session.setAttribute("message", new Message("Something went wrong !!! Try Again", "alert-danger"));
		return "index";
	}
	 
		
	}
	
	@GetMapping("/login2")
	public String patientLogin() {
		
		return "patientLoginForm";
	}
	
	@PostMapping("/do_login2")
	public String doLogin2(@RequestParam("username") String username,
			               @RequestParam("password") String password,
			               Model model,
			               HttpSession session) {
		
	try {	
	    Patients patients=patientsRepository.getPatientsByUsername(username);
		
		String Username=patients.getUsername();
		String p=patients.getPassword();
		String Name=patients.getName();
		
		boolean isPasswordMatch=bCryptPasswordEncoder.matches(password, p);
		System.out.println(isPasswordMatch);
		
		if(Username.equals(username) && isPasswordMatch==true) {
			
			model.addAttribute("username", username);
			
			session.setAttribute("message", new Message("Welcome "+Name+" !!! You logged in successfully", "alert-success"));
			return "patientDashboard";
		}
		
		else {
			
			session.setAttribute("message", new Message("Wrong credentials !!!, Try again", "alert-danger"));
			return "patientLoginForm";
		}
	}
	
	catch(Exception e) {
		
		session.setAttribute("message", new Message("Wrong credentials !!!, Try again", "alert-danger"));
		return "patientLoginForm";
	}
		
	}
	
	@GetMapping("/chooseSpecialist")
	public String chooseSpecialistForm(@RequestParam("username") String username,
			                           Model model) {
		
		Patients patients=patientsRepository.getPatientsByUsername(username);
		//System.out.println(username);
		model.addAttribute("username",username);
		
		return "chooseSpecializationForm";
		
	}
	
	@PostMapping("/processChooseSpecialist")
	public String processChooseSpecialist(@RequestParam("username") String username,
			                              @RequestParam("specialist") String specialist,
			                              Model model) {
		
		Doctor doctor=doctorRepository.getDoctorBySpecialization(specialist);
		Patients patients=patientsRepository.getPatientsByUsername(username);
		int patientId=patients.getId();
		
		int dId=doctor.getId();
		
		List<DoctorAvailabilityDates> d=doctorAvailabilityDatesRepository.getDoctorAvailabilityDatesByDoctor(dId);
		
		model.addAttribute("dates",d);
		model.addAttribute("doctorId", dId);
		model.addAttribute("patientId", patientId);
		
		return "viewAvailableSlots";
		
	}
	
	@PostMapping("/bookSlot/{doctorId}/{patientId}/{slotId}")
	public String processBookSlot(@PathVariable("doctorId") Integer doctorId,
			                      @PathVariable("patientId") Integer patientId,
			                      @PathVariable("slotId") Integer slotId,
			                      HttpSession session) {
		
	try {
		Patients patients=patientsRepository.getById(patientId);
		
		Doctor doctor=doctorRepository.getById(doctorId);
		DoctorAvailabilityDates doctorAvailabilityDates=doctorAvailabilityDatesRepository.getById(slotId);
		
		int limit=patientsRepository.getCountByColumnName(slotId);
		System.out.println(limit);
		
		if(limit<2) {
			
		    patients.setDoctor(doctor);
		    patients.setDAvailabilityDates(doctorAvailabilityDates);
		
		    patientsRepository.save(patients);
		
		}
		
		else {
			
			session.setAttribute("message",new Message("Sorry !!! This slot is full... Choose another slot", "alert-danger"));
			return "redirect:/indexPage";
		}
		
		session.setAttribute("message",new Message("Slot booked successfully !!!", "alert-success"));
		return "redirect:/indexPage";
	}
	catch(Exception e) {
		
	    session.setAttribute("message", new Message("Something went wrong !!! Try again", "alert-danger"));
		return "redirect:/indexPage";
	}
		
	}
	
	
	@PostMapping("/cancelAppointment")
	public String cancelAppointment(@RequestParam("username") String username,
			                        Model model) {
		
		Patients patients=patientsRepository.getPatientsByUsername(username);
		model.addAttribute("patients", patients);
		
		return "deleteAppointment";
		
	}
	
	@PostMapping("/processDeleteAppointment/{id}")
	public String processDeleteAppointment(@PathVariable("id") Integer id,
			                               HttpSession session) {
		
		patientsRepository.deleteColumnsById(id);
		
		session.setAttribute("message", new Message("Appointment cancelled successfully !!!", "alert-success"));
		return "redirect:/indexPage";
	}
	
	@PostMapping("/editProfile")
	public String editInfo(@RequestParam("username") String username,
			               Model model) {
		
		Patients patients=patientsRepository.getPatientsByUsername(username);
		int id=patients.getId();
		
		model.addAttribute("id", id);
		return "editProfileForm";
		
	}
	
	@PostMapping("/do_profileUpdate")
	public String processProfileUpdate(@RequestParam("id") Integer id,
			                           @RequestParam("name") String name,
			                           @RequestParam("username") String username
			                           ) {
		System.out.println(id);
		
		Patients patients=patientsRepository.getById(id);
		
		patients.setName(name);
		patients.setUsername(username);
		
		
		patientsRepository.save(patients);
		
		return "index";
		
	}
	
	
	

}
