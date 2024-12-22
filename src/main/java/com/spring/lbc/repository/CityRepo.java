package com.spring.lbc.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.lbc.model.Cities;

public interface CityRepo extends JpaRepository<Cities, Long> {

	List<Cities> findByStateId(Long stateId);

	String findByName(String city);

}
