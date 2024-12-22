package com.spring.lbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.lbc.model.States;

public interface StatesRepo extends JpaRepository<States, Long> {

	List<States> findByCountryId(Long countryId);

	States findByName(String state);

}
