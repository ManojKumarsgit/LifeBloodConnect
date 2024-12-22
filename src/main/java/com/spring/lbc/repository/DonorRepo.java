package com.spring.lbc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.lbc.model.Bloods;
import com.spring.lbc.model.Cities;
import com.spring.lbc.model.Donors;

public interface DonorRepo extends JpaRepository<Donors, Long> {

	List<Donors> findByCityAndBgroupAndIsAvail(String cities, String bloodGroup, String string);

	Donors findByEmail(String email);

}
