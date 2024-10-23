package com.example.CRUD.service;

import java.util.List;

import com.example.mo.Concert;

public interface ConcertService {
    List<Concert> getAllConcerts();

    void voteForConcert(Integer concertId, Integer userId);

    Concert getConcertById(Integer id);

    Concert saveConcert(Concert concert);

    void deleteConcert(Integer id);
    void updateAverageRating(Integer concertID);
    boolean isDuplicateTitle(String translatedTitle);
    boolean concertExistsByTitle(String title);
    List<Concert> getAllComingSoonConcerts();
    List<Concert> getConcertsByGenre(String genre);
    List<Concert> findByConcertOwnerID(int yardId);
}