package com.spring.lbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.lbc.model.Admins;

public interface AdminRepo extends JpaRepository<Admins, Long> {

	Admins findByEmail(String email);

}
