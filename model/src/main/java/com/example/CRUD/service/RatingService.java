package com.example.CRUD.service;

import java.util.List;

import com.example.mo.Rating;
import com.example.mo.Users;

public interface RatingService {
    Rating saveRating(Rating rating);
    List<Rating> getAllRatingsByConcertId(Integer concertId);
    Rating getRatingByUserAndConcert(Users user, Integer concertId);
    void updateAverageRating(Integer concertID);
    Rating getRatingById(Integer ratingId);
    void deleteRating(Integer ratingId);
}