package com.spring.lbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.lbc.model.Countries;


public interface CountryRepo extends JpaRepository<Countries, Long> {

	Countries findByName(String country);

}
