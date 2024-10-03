package com.example.crud.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mo.ConcertOwner;

@Repository
public interface ConcertOwnerRepository extends JpaRepository<ConcertOwner, Integer> {
    List<ConcertOwner> findByConcertNameContainingIgnoreCase(String cinemaName);
}