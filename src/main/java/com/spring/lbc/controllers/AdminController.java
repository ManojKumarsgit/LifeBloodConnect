package com.spring.lbc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.lbc.model.Countries;
import com.spring.lbc.model.Donors;
import com.spring.lbc.model.Reports;
import com.spring.lbc.repository.CountryRepo;
import com.spring.lbc.repository.DonorRepo;
import com.spring.lbc.repository.ReportsRepo;
import com.spring.lbc.service.DonorService;



@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	public CountryRepo cnRepo;
	
	@Autowired
	public DonorService doService;

	@Autowired
	public DonorRepo donorRepository;
	
	@Autowired
	public ReportsRepo reportRepository;
	
	@GetMapping("/manageDonors")
	public String manageDonors(Model model) {
		List<Donors> donors = donorRepository.findAll();
		model.addAttribute("donors",donors);
		return "/admin/manageDonors";
	}
	
	@PostMapping("/search/show")
	public String searchShow(String email, Model model, RedirectAttributes ra) {
		Donors donors = donorRepository.findByEmail(email);
		if(donors == null) {
			ra.addFlashAttribute("error");
		} else {
			ra.addFlashAttribute("search",donors);
		}
		return "redirect:/admin/manageDonors";
	}
	
	@PostMapping("/logout")
	public String logout() {
		return "redirect:/admin/login";
	}
	
	
	
	@GetMapping("/manageReports")
	public String showReports(Model model) {
		List<Reports> reports = reportRepository.findAll();
		model.addAttribute("reports",reports);
		return "/admin/reports";
	}
	
	@PostMapping("/reports/update/{id}")
	public String updateStatus(@PathVariable Long id, String status) {
		doService.updateReports(id, status);
		return "redirect:/admin/manageReports";
	}
	
	@PostMapping("/profile/delete/{id}")
	public String deleteDonor(@PathVariable Long id) {
		donorRepository.deleteById(id);
		return "redirect:/admin/manageDonors";
	}
	
	@GetMapping("/profile/edit/{id}")
	public String editProfile(@PathVariable Long id, Model model) {
		Donors donor = donorRepository.findById(id).orElse(null);
		
		 List<Countries> countries = cnRepo.findAll();
		 model.addAttribute("donor",donor);
		 model.addAttribute("countries",countries);
		 model.addAttribute("bloods",doService.getBloodGroups());
		return "/admin/edit";
	}
	
	@PostMapping("/profile/update/{id}")
	public String updateProfile(@PathVariable Long id,Donors donor) {
		doService.updateProfile(id, donor);
		return "redirect:/admin/manageDonors";
	}
	
	@GetMapping("/login")
	public String login(){
		return "/admin/login";
	}
	
	@PostMapping("/login/check")
	public String profileCheck(String email, String password, RedirectAttributes ra, Model model) {
		
		 if(doService.isAdmin(email, password) == "success") {
			 model.addAttribute("success", "You're Logged in");
			 return "redirect:/admin/manageDonors";
		 }
		 ra.addFlashAttribute("error", "Check login credintials");
		 return "redirect:/admin/login";
	}
}
