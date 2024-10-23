package com.example.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.RatingRepository;
import com.example.mo.Concert;
import com.example.mo.Rating;
import com.example.mo.Users;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ConcertService concertService;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, ConcertService concertService) {
        this.ratingRepository = ratingRepository;
        this.concertService = concertService;
    }

    @Override
    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatingsByConcertId(Integer concertId) {
        return ratingRepository.findByConcert_ConcertID(concertId);
    }

    

    @Override
    public void updateAverageRating(Integer concertID) {
        List<Rating> ratings = ratingRepository.findByConcert_ConcertID(concertID);
        double averageRating = ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
        Concert concert = concertService.getConcertById(concertID); // Use concertService to get concert by ID
        if (concert != null) {
            concert.setAverageRating(averageRating);
            concert.setRatingCount(ratings.size());
            concertService.saveConcert(concert);
        }
    }
     @Override
    public Rating getRatingByUserAndConcert(Users user, Integer concertId) {
        return ratingRepository.findByUserAndConcert_ConcertID(user, concertId);
    }
    @Override
    public Rating getRatingById(Integer ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }

    @Override
    public void deleteRating(Integer ratingId) {
        ratingRepository.deleteById(ratingId);
    }
    
}