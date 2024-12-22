package com.spring.lbc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.lbc.model.Cities;
import com.spring.lbc.model.Countries;
import com.spring.lbc.model.States;
import com.spring.lbc.repository.CityRepo;
import com.spring.lbc.repository.CountryRepo;
import com.spring.lbc.repository.StatesRepo;

@RestController
public class MyController {

    @Autowired
    private CountryRepo countryRepository;

    @Autowired
    private StatesRepo stateRepository;

    @Autowired
    private CityRepo cityRepository;

    @GetMapping("/fetchStates")
    public ResponseEntity<List<States>> fetchStates(@RequestParam Long countryId) {
        List<States> states = stateRepository.findByCountryId(countryId);
        return ResponseEntity.ok(states);
    }

    @GetMapping("/fetchCities")
    public ResponseEntity<List<Cities>> fetchCities(@RequestParam Long stateId) {
        List<Cities> cities = cityRepository.findByStateId(stateId);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/getCountries")
    public ResponseEntity<List<Countries>> getCountries() {
        List<Countries> countries = countryRepository.findAll();
        
        return ResponseEntity.ok(countries);
    }

   
}
