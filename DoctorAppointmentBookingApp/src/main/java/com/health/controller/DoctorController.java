package com.health.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.health.entity.Doctor;
import com.health.entity.DoctorAvailabilityDates;
import com.health.entity.Patients;
import com.health.helper.Message;
import com.health.repository.DoctorAvailabilityDatesRepository;
import com.health.repository.DoctorRepository;
import com.health.repository.PatientsRepository;

import jakarta.servlet.http.HttpSession;

@Controller

public class DoctorController {
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientsRepository patientsRepository;
	
	@Autowired
	private DoctorAvailabilityDatesRepository doctorAvailabilityDatesRepository;
	
	@RequestMapping("/doctor/doctorDashboard")
	public String doctorDashboard() {
		
		return "doctorDashboard";
	}
	
	@GetMapping("/addAvailableDays")
	public String addAvailableDays() {
		
		return "AvailableDaysForm";
	}
	
	@PostMapping("/processAvailabilityDate")
	public String processAvailabilityDate(@ModelAttribute DoctorAvailabilityDates doctorAvailabilityDates,
			                              @RequestParam("timeFrom") String timeFrom,
			                              @RequestParam("timeTo") String timeTo,
			                              @RequestParam("id") Integer id,
			                              HttpSession session) {
		
	try {
		
		Doctor doctor=doctorRepository.getById(id);
		
	     
		doctorAvailabilityDates.setDoctor(doctor);
		doctorAvailabilityDates.setAvailableDateAndTimeFrom(timeFrom);
		doctorAvailabilityDates.setAvailableDateAndTimeTo(timeTo);
		
		doctorAvailabilityDatesRepository.save(doctorAvailabilityDates);
		
		session.setAttribute("message", new Message("Availability day and time added successfully !!!","alert-success"));
		return "redirect:/addAvailableDays";
		
	   }
	
	catch(Exception e) {
		
		session.setAttribute("message", new Message("Something went wrong !!! Try Again", "alert-danger"));
		return "redirect:/addAvailableDays";
	}
}
	
	@GetMapping("/viewBookedSlots/{dId}")
	public String viewBookedSlots(@PathVariable("dId") Integer dId,
			                      Model model) {
		
		Doctor doctor=doctorRepository.getById(dId);
		
		
		List<Patients> patients=patientsRepository.getPatientsByDoctor(dId);
		
		model.addAttribute("patients", patients);
		
		return "viewPatients";
		
		
	}
	
	@GetMapping("/viewTimings/{slotId}")
	public String viewTimings(@PathVariable("slotId") Integer slotId,
			                  Model model) {
		
		DoctorAvailabilityDates doctorAvailabilityDates=doctorAvailabilityDatesRepository.getById(slotId);
		
		model.addAttribute("time", doctorAvailabilityDates);
		
		return "viewTimings";
		
	}
	
	@GetMapping("/viewTodaysSlots/{dId}")
	public String viewTodaysSlotsBooked(@PathVariable("dId") Integer dId,
			                            Model model) {
		
		List<Patients> patients=patientsRepository.findByTodayDate(dId);
		
		model.addAttribute("patients", patients);
		
		return "viewTodaysSlots";
	}
	
	@GetMapping("/deleteVisitedPatientsSlotAndDoctor/{id}")
	public String deleteVisitedPatientsSlotAndDoctor(@PathVariable("id") Integer id,
			                                          HttpSession session) {
		
	try {
		
		patientsRepository.deleteColumnsById(id);
		
		session.setAttribute("message", new Message("Patient's slot and doctor id deleted successfully !!!", "alert-success"));
		return "redirect:/indexPage";
	    }
	
	catch (Exception e) {
		
		session.setAttribute("message", new Message("Something went wrong !!! Try Again", "alert-danger"));
		return "redirect:/indexPage";
		
	    }
	}
	
	
	
	
	

}
