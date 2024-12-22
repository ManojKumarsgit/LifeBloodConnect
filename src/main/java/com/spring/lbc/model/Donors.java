package com.spring.lbc.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Donors {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	 
	    private String name;

	    
	    private String email;

	   
	    private String password;

	  
	    private String phone;
	
	    
	    private String country;

	    
	    private String state;

	  
	    private String city;

	   
	    private String bgroup;
	    
	   
	    private String isAvail;
	    private String lastDonated;
	    private String dateofbirth;
	    private String gender;
	    private String age;
	    private String createdAt;
	    private String updatedAt;
}
