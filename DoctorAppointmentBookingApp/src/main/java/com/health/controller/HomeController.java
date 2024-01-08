package com.health.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.health.entity.Admin;
import com.health.entity.Doctor;
import com.health.helper.Message;
import com.health.repository.AdminRepository;
import com.health.repository.DoctorRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/indexPage")
	public String indexPage() {
		
		return "index";
	}
	
	@GetMapping("/adminLoginForm")
	public String adminLoginForm() {
		
		return "adminLoginForm";
	}
	
	@PostMapping("/processAdminLoginForm")
	public String processAdminLoginForm(@RequestParam("username") String Username,
			                            @RequestParam("password") String Password,
			                            HttpSession session) {
		
	try {
		Admin a=adminRepository.getAdminByUsername(Username);
		
		String username=a.getUsername();
		String password=a.getPassword();
		
		if(Username.equals(username) && Password.equals(password)) {
			
			session.setAttribute("message",new Message("You logged in successfully !!!","alert-success"));
			return "adminDashboard";
			
		    }
		
		session.setAttribute("message", new Message("Wrong credentials !!! Try again","alert-danger"));
        return "adminLoginForm";
        
	    }
		
		catch(Exception e) {
			
		session.setAttribute("message", new Message("Wrong credentials !!! Try again","alert-danger"));
		return "adminLoginForm";
		
		}
	}
	
	@GetMapping("/addDoctors")
	public String addDoctor() {
		
		return "addDoctorForm";
	}
	
	@PostMapping("/processAddDoctorForm")
	public String processAddDoctor(@ModelAttribute Doctor doctor,
			                       @RequestParam("username") String Username,
			                       @RequestParam("password") String Password,
			                       @RequestParam("name") String Name,
			                       @RequestParam("specialization") String Specialization,
			                       HttpSession session) {
		try {
		
		  Admin admin=adminRepository.getById(1);
		
		  doctor.setAdmin(admin);
		  doctor.setRole("ROLE_DOCTOR");
		  doctor.setUsername(Username);
		  doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
		  doctor.setSpecialization(Specialization);
		  doctor.setName(Name);
		
		  doctorRepository.save(doctor);
		
		  System.out.println("Doctor Added Successfully");
		
		  session.setAttribute("message",new Message("Doctor added successfully !!!","alert-success"));
		  return "addDoctorForm";
		  
		}
		
		catch(Exception e) {
			
			session.setAttribute("message", new Message("Something went wrong !!! Try Again","alert-danger"));
			return "addDoctorForm";
		}
		
		
	}
	
	@GetMapping("/viewDoctors")
	public String viewDoctors(@ModelAttribute Doctor doctor,
			                  Model model) {
		
		List<Doctor> d=doctorRepository.findAll();
		model.addAttribute("doctor", d);
		
		return "viewAllDoctors";
		
	}
	
	
	@GetMapping("/login1")
	public String doctorLogin() {
		
		return "doctorLoginForm";
	}
	
	
	@PostMapping("/do_login1")
	public String doDoctorLogin(@RequestParam("username") String username,
			                    @RequestParam("password") String password,
			                    Model model,
			                    HttpSession session) {
		
		
  try {
		Doctor doctor=doctorRepository.getDoctorByUsername(username);
		String Name=doctorRepository.getName(username);
		String Username=doctor.getUsername();
		int dId=doctor.getId();
		System.out.println(Username);
		String P=doctor.getPassword();
		boolean isPasswordMatch=passwordEncoder.matches(password, P);
		
		
		
		
		if(Username.equals(username) && isPasswordMatch==true) {
			
			//model.addAttribute("username",username);
			model.addAttribute("dId", dId);
			
			session.setAttribute("message",new Message("Welcome....Dr."+Name+" !!!", "alert-success"));
			return "doctorDashboard";
		}
		
		else {
		
			session.setAttribute("message", new Message("Wrong Credentials !!! Try Again","alert-danger"));
		    return "index";
		
		}
	}
	
	catch(Exception e) {
		
		session.setAttribute("message", new Message("Wrong Credentials !!! Try Again","alert-danger"));
	    return "index";
		
	}
		
	}
	
	
	
	
		
	
	
}
