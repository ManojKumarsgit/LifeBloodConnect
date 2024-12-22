package com.spring.lbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.lbc.model.Bloods;

public interface BloodRepo extends JpaRepository<Bloods, Long> {


	 

}
