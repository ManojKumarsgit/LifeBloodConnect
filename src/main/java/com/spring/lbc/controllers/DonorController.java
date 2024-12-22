package com.spring.lbc.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.lbc.model.Countries;
import com.spring.lbc.model.Donors;
import com.spring.lbc.model.Reports;
import com.spring.lbc.repository.CountryRepo;
import com.spring.lbc.repository.DonorRepo;
import com.spring.lbc.repository.ReportsRepo;
import com.spring.lbc.service.DonorService;

import jakarta.validation.Valid;


@Controller
public class DonorController {

	@Autowired
	public CountryRepo cnRepo;
	
	@Autowired
	public DonorService doService;

	@Autowired
	public DonorRepo donorRepository;
    
	@Autowired
	public ReportsRepo reportsRepository;
    
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/donor/request")
	public String request(Model model) {
		List<Countries> countries = cnRepo.findAll();
		model.addAttribute("bloods",doService.getBloodGroups());
		model.addAttribute("countries",countries);
		return "request";
	}
	
	@PostMapping("/donor/search")
	public String searchBlood(Long bgroup, Long city, Model model) {
		List<Donors> donors = doService.searchShow(bgroup, city);
		model.addAttribute("search",donors);
		return "donorSearch";
	}
	
	
	 @PostMapping("/donor/report")
	 public String saveReports(Reports report, Model model) {
		if(doService.reportCheck(report) == "error") {
			model.addAttribute("error","Maximum reports reached for this donor.");
		} else {
			model.addAttribute("success", "Report submitted successfully.");
		}
		return "reportStatus";
	}
	
	 @GetMapping("/donor/registerForm")
	 public String showRegisterForm(Model model) {
		 List<Countries> countries = cnRepo.findAll();
		 model.addAttribute("countries",countries);
		 model.addAttribute("bloods",doService.getBloodGroups());
		 return "register";
	 }
	 
	 @PostMapping("/donor/store")
	 public String registerDonor( Donors donor, Model model, String password2, RedirectAttributes ra  ) {
		 if( doService.validate(donor, model,password2,ra ) == "success") {
			 model.addAttribute("success", "Donor Registered Succesfully");
			 return "redirect:/donor/login";
		 }
		 return doService.validate(donor, model,password2,ra );
	 }
	 
	 @GetMapping("/donor/login")
	 public String login() {
		 return "login";
	 }
	 
	 @PostMapping("/donor/login/check")
	 public String check( String email,String password, Model model, RedirectAttributes ra ) {
		 if(doService.checkLogged(email, password) == "success") {
			 model.addAttribute("success", "You're Logged in");
			 Donors donor = doService.getDonor(email);
			 return "redirect:/donor/profile/" + donor.getId();
		 }
		 ra.addFlashAttribute("error", "Check login credintials");
		 return "redirect:/donor/login";
	 }
	 
	 @GetMapping("/donor/profile/{id}")
	 public String showProfile(@PathVariable Long id, Model model) {
		 Donors donor = doService.getDonorById(id);
		 model.addAttribute("donor",donor);
		 return "dashboard";
	 }
	 
	 @GetMapping("/donor/profile/edit/{id}")
	 public String editProfile(@PathVariable Long id, Model model) {
		 Donors donor = doService.getDonorById(id);
		 List<Countries> countries = cnRepo.findAll();
		 model.addAttribute("donor",donor);
		 model.addAttribute("countries",countries);
		 model.addAttribute("bloods",doService.getBloodGroups());
		 return "editProfile";
	 }
	 
	 @PostMapping("/donor/profile/update/{id}")
	 public String updateProfile(@PathVariable Long id, Donors donor, Model model) {
		 doService.updateProfile(id, donor);
		 model.addAttribute("donor",donor);
		 return "dashboard";	 
	}
	 
	 @PostMapping("/donor/profile/delete/{id}")
	 public String deleteProfile(@PathVariable Long id) {
		 donorRepository.deleteById(id);
		 return "redirect:/donor/login";
	 }
	 
	 @PostMapping("/donor/profile/logout")
	 public String logout() {
		 return "redirect:/donor/login";
	 }
	 
	 @GetMapping("/donor/profile/report/{id}")
	 public String showReports(@PathVariable Long id, Model model) {
		 Donors donor = doService.getDonorById(id);
		 model.addAttribute("donor",donor);
		 model.addAttribute("reports",doService.getReports(donor.getEmail()));
		 return "report";
	 }
	 
	 @PostMapping("/donor/reports/update/{id}")
		public String updateStatus(@PathVariable Long id, String status) {
		 	Reports report = reportsRepository.findById(id).orElse(null);
		 	Donors donor = donorRepository.findByEmail(report.getEmail());
			doService.updateReports(id, status);
			
			return "redirect:/donor/profile/report/"+donor.getId();
	}
}
