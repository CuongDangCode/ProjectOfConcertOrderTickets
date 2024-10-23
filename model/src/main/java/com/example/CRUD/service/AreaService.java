package com.example.CRUD.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.AreaRepository;
import com.example.CRUD.controller.AreaNotFoundException;
import com.example.mo.Area;




@Service
public class AreaService {
    @Autowired
    private AreaRepository repo;

     public List<Area> listAll() {
        return (List<Area>) repo.findAll();
    }

     public void save(Area area) {
        repo.save(area);
    }

     public Area get(Integer areaID) throws AreaNotFoundException {
        Optional<Area> result = repo.findById(areaID);
        if (result.isPresent()) {
            return result.get();
        }
        throw new AreaNotFoundException("Could not find any Area with ID " + areaID);
    }


     public void delete(Integer AreaID) throws AreaNotFoundException {
        if (!repo.existsById(AreaID)) {
            throw new AreaNotFoundException("Could not find any yard with ID " + AreaID);
        }
        repo.deleteById(AreaID);
    } 

}