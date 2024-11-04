package com.example.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.repository.ConcertOwnerRepository;
import com.example.mo.ConcertOwner;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class ConcertOwnerService {
    
    @Autowired
    private ConcertOwnerRepository concertOwnerRepository;

    public List<ConcertOwner> getConcertOwnerByAdmins() {
        return concertOwnerRepository.findAll();
    }

    public List<ConcertOwner> searchConcertsOwnerByConcertName(String concertName) {
        if (concertName == null || concertName.isEmpty()) {
            return concertOwnerRepository.findAll();
        } else {
            List<ConcertOwner> results = concertOwnerRepository.findByConcertNameContainingIgnoreCase(concertName);
            if (results.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No concert owners found for the provided name");
            }
            return results;
        }
    }
}
