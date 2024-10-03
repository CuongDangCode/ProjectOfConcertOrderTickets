package com.example.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mo.Concert;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    
}