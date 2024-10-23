package com.example.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.YardRepository;
import com.example.CRUD.controller.YardNotFoundException;
import com.example.mo.Yard;

@Service
public class YardService {

    @Autowired
    private YardRepository repo;

    public List<Yard> listAll() {
        return repo.findAll();
    }

    public List<Yard> listAllByConcertOwnerID(Integer concertOwnerID) {
        return repo.findByConcertOwnerID(concertOwnerID);
    }

    public Yard save(Yard yard) {
        return repo.save(yard);
    }

    public Yard get(Integer yardID) throws YardNotFoundException {
        return repo.findById(yardID).orElseThrow(() -> new YardNotFoundException("Yard not found"));
    }

    public void delete(Integer yardID) throws YardNotFoundException {
        if (!repo.existsById(yardID)) {
            throw new YardNotFoundException("Yard not found");
        }
        repo.deleteById(yardID);
    }

    public Integer findConcertOwnerIdByYardId(Integer yardId) {
        return repo.findConcertOwnerIdByYardId(yardId);
    }
    public List<Yard> getAllYards() {
        return repo.findAll();
    }

    public List<Yard> searchYards(String keyword) {
        return repo.findByYardNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, keyword);
    }

    public List<Yard> searchYardsByLocation(String location) {
        return repo.findByAddressContainingIgnoreCase(location);
    }

    public List<Yard> getYardsByConcertID(Integer concertID) {
        return repo.findYardsByConcertID(concertID);
    }
}
