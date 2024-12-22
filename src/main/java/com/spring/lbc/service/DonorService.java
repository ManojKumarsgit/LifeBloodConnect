package com.spring.lbc.service;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.mindrot.jbcrypt.BCrypt;

import com.spring.lbc.PasswordEncoder;
import com.spring.lbc.model.Admins;
import com.spring.lbc.model.Bloods;
import com.spring.lbc.model.Cities;
import com.spring.lbc.model.Countries;
import com.spring.lbc.model.Donors;
import com.spring.lbc.model.Reports;
import com.spring.lbc.model.States;
import com.spring.lbc.repository.AdminRepo;
import com.spring.lbc.repository.BloodRepo;
import com.spring.lbc.repository.CityRepo;
import com.spring.lbc.repository.CountryRepo;
import com.spring.lbc.repository.DonorRepo;
import com.spring.lbc.repository.ReportsRepo;
import com.spring.lbc.repository.StatesRepo;




@Service
public class DonorService {
	
	@Autowired
	public BloodRepo bloodRepo;

    @Autowired
    private CityRepo cityRepository;
    @Autowired
    private CountryRepo countryRepository;
    @Autowired
    private StatesRepo stateRepository;
    
    @Autowired
    private DonorRepo donorRepository;
    
    @Autowired
    private ReportsRepo reportRepository;
    
    @Autowired
    private AdminRepo adminRepository;
    

	public List<Bloods> getBloodGroups(){
		List<Bloods> bloodGroups = bloodRepo.findAll();
		return bloodGroups;
	}
	
    public List<Donors> searchShow(Long bgroup,Long  city) {
        Bloods bloodGroup = bloodRepo.findById(bgroup).orElse(null);
        Cities cities = cityRepository.findById(city).orElse(null);

        List<Donors> searchResults = donorRepository.findByCityAndBgroupAndIsAvail(cities.getName(), bloodGroup.getBgroup(), "Available");
        return searchResults;
    }
    public String reportCheck(Reports reports) {
		
		// Check the number of reports submitted by the donor
		int reportCount = reportRepository.countByEmail(reports.getEmail());
		
		if (reportCount >= 5) {
			return "error";
		}
//		
		// Create a new report entry
		Reports report = new Reports();
		report.setName(reports.getName());
		report.setEmail(reports.getEmail());
		report.setReason(reports.getReason());
		report.setInformation(reports.getInformation());
		report.setStatus("Not Cleared Yet");
		report.setCount(reportCount + 1);
		reportRepository.save(report);
		
			return "success";
		}

	public String validate(Donors donor, Model model, String password2, RedirectAttributes ra) {
		 
			Donors donor1 = new Donors();
	        LocalDate today = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDate birthDate = LocalDate.parse(donor.getDateofbirth(), formatter);
	        int age = Period.between(birthDate, today).getYears();

	        if (age < 18 || age > 65) {
	            ra.addFlashAttribute("age","Age Must me Above 18 or below 65");
	             return "redirect:/donor/registerForm";
	        }
	        if(!donor.getPassword().equals(password2)) {
	        	 ra.addFlashAttribute("error", "Password does not match");
	             return "redirect:/donor/registerForm";
	        }
	        if(donor.getPassword().length() < 8) {
	        	 ra.addFlashAttribute("error", "Password must contains 8 or more characters");
	             return "redirect:/donor/registerForm";
	        }
	        if(donor.getPhone().length() < 10) {
	        	 ra.addFlashAttribute("phone","Phone number would be 10 digits");
	             return "redirect:/donor/registerForm";
	        }
	        if(String.valueOf(donor.getLastDonated()).isEmpty()) {
	        	 donor1.setLastDonated("Not Provided");
	        } else {
	        	donor1.setLastDonated(String.valueOf(donor.getLastDonated()));
	        }
	        Cities cities = cityRepository.findById(Long.parseLong(donor.getCity())).orElse(null);
	        Countries countries = countryRepository.findById(Long.parseLong(donor.getCountry())).orElse(null);
	        States states = stateRepository.findById(Long.parseLong(donor.getState())).orElse(null);
	        Bloods bloodGroup = bloodRepo.findById(Long.parseLong(donor.getBgroup())).orElse(null);
	        System.out.println(donor.getIsAvail());
	        System.out.println(donor.getLastDonated());
	        donor1.setName(donor.getName());
	        donor1.setEmail(donor.getEmail());
	        donor1.setPhone(donor.getPhone());
	        donor1.setCountry(countries.getName());
	        donor1.setState(states.getName());
	        donor1.setCity(cities.getName());
	        donor1.setAge(String.valueOf(age));
	        donor1.setPassword(PasswordEncoder.encodePassword(password2));
	        donor1.setBgroup(bloodGroup.getBgroup());
	        donor1.setDateofbirth(donor.getDateofbirth());
	        donor1.setIsAvail(donor.getIsAvail());
	        donor1.setGender(donor.getGender());
	        
	      donorRepository.save(donor1);  
	       return "success";
	}

	public String checkLogged(String email, String password) {
	
		Donors donors = donorRepository.findByEmail(email);
		if(donors == null) {
			return "error";
		}
		
		if(email.equals(donors.getEmail()) && PasswordEncoder.verifyPassword(password, donors.getPassword())) {
			return "success";
		} else {
			return "Check login creditials";
		}
		
	}

	public Donors getDonorById(Long id) {
		return donorRepository.findById(id).orElse(null);
	}

	public Donors getDonor(String email) {
		return donorRepository.findByEmail(email);
	}

	public void updateProfile(Long id, Donors donor) {
		Cities cities = cityRepository.findById(Long.parseLong(donor.getCity())).orElse(null);
        Countries countries = countryRepository.findById(Long.parseLong(donor.getCountry())).orElse(null);
        States states = stateRepository.findById(Long.parseLong(donor.getState())).orElse(null);
        Bloods bloodGroup = bloodRepo.findById(Long.parseLong(donor.getBgroup())).orElse(null);
		Donors donors  = donorRepository.findById(id).orElse(null);
		 donor.setPassword(donors.getPassword());
		 donor.setId(donors.getId());
		 donor.setCountry(countries.getName());
	     donor.setState(states.getName());
	     donor.setCity(cities.getName());
	     donor.setBgroup(bloodGroup.getBgroup());
	     if(String.valueOf(donor.getLastDonated()).isEmpty()) {
        	 donor.setLastDonated("Not Provided");
        } else {
        	donor.setLastDonated(String.valueOf(donor.getLastDonated()));
        }
	    donor.setAge(donors.getAge());

		 donorRepository.save(donor);
		
	}

	public List<Reports> getReports(String email) {
		return reportRepository.findByEmailOrderByIdDesc(email);
		
	}

	public String isAdmin(String email, String password) {
		String pass = PasswordEncoder.encodePassword(password);
		System.out.println(pass);
		Admins admin = adminRepository.findByEmail(email);
		if(admin == null) {
			return "error";
		}
		
		if(email.equals(admin.getEmail()) && PasswordEncoder.verifyPassword(password, admin.getPassword())) {
			return "success";
		} else {
			return "Check login creditials";
		}
		
	}
	
	public void updateReports(Long id, String status) {
		Reports report = reportRepository.findById(id).orElse(null);
		if(report.getCount() != 0) {
			report.setCount(report.getCount()-1);
		}
		report.setStatus(status);
		reportRepository.save(report);
	}

	

	
    
    
   }
