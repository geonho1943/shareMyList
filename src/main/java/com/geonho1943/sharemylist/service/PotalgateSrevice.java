package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.Potalgate;
import com.geonho1943.sharemylist.repository.PotalgateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotalgateSrevice {
    @Autowired
    private PotalgateRepository potalgateRepository;

    public List<Potalgate> getAllPotal(){
        return potalgateRepository.findAll();
    }
}
