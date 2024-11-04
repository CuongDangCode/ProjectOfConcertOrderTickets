package com.example.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.repository.ConcertOwnerRepository;
import com.example.mo.Concert;
import com.example.mo.ConcertOwner;
@Service
public interface ConcertService {

    @Autowired
    private ConcertOwnerRepository concertOwnerRepository;
    public List<ConcertOwner> getConcertOwnerByAdmins(){
        return concertOwnerRepository.findAll();
    }
    public List<ConcertOwner> searchConcertOwnerByConcertName(String concertName) {
        if (concertName == null || concertName.isEmpty()) {
            return concertOwnerRepository.findAll();
        } else {
            return concertOwnerRepository.findByConcertNameContainingIgnoreCase(concertName);
        }
    }

    List<Concert> getAllConcerts();

    Concert getConcertById(Integer id);

    Concert saveConcert(Concert Concert);

    void deleteConcert(Integer id);
}