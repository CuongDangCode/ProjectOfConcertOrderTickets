package com.example.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.ConcertOwnerRepository;
import com.example.mo.ConcertOwner;

@Service
public class CinemaService {
    @Autowired
    private ConcertOwnerRepository concertOwnerRepository;

    public List<ConcertOwner> getConcertOwnerByAdmins(){
        return concertOwnerRepository.findAll();
    }

    // public List<ConcertOwner> searchConcertsOwnerByCinemaName(String cinemaName) {
    //     if (cinemaName == null || cinemaName.isEmpty()) {
    //         return concertOwnerRepository.findAll();
    //     } else {
    //         return concertOwnerRepository.findByCinemaNameContainingIgnoreCase(cinemaName);
    //     }
    // }

    public ConcertOwner getConcertOwnerById(int concertOwnerId) {
        return concertOwnerRepository.findById(concertOwnerId).orElse(null);
    }

    public void saveConcertOwner(ConcertOwner concertOwner) {
        concertOwnerRepository.save(concertOwner);
    }
}
