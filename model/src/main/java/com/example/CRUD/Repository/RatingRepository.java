package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mo.Rating;
import com.example.mo.Users;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByConcert_ConcertID(Integer concertID);
    Rating findByUserAndConcert_ConcertID(Users user, Integer concertId);
}