package com.spring.lbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.lbc.model.Reports;

public interface ReportsRepo extends JpaRepository<Reports, Long> {

	int countByEmail(String email);
	
	List<Reports> findByEmailOrderByIdDesc(String email);

}
