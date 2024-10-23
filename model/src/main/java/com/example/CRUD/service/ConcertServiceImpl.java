package com.example.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.ConcertRepository;
import com.example.CRUD.Repository.RatingRepository;
import com.example.CRUD.Repository.UserRepository;
import com.example.mo.Concert;
import com.example.mo.Rating;
import com.example.mo.Users;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public ConcertServiceImpl (ConcertRepository concertRepository, UserRepository userRepository,RatingRepository ratingRepository) {
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

  
    @Override
    public Concert getConcertById(Integer concertID) {
        return concertRepository.findById(concertID).orElse(null);
    }

    @Override
    public Concert saveConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    @Override
    public void deleteConcert(Integer id) {
        concertRepository.deleteById(id);
    }
    public void voteForConcert(Integer concertId, Integer userId) {
        Concert concert = getConcertById(concertId);
        Users user = userRepository.findById(userId).orElse(null);

        if (concert == null || user == null) {
            throw new RuntimeException("Concert or User not found");
        }

        if (concert.getConcertOwnerVotes().contains(userId)) {
            throw new RuntimeException("User has already voted for this concert");
        }

        concert.addVote(userId);
        saveConcert(concert);

        // Check if all concert owners have voted
        long totalConcertOwners = userRepository.countByRole("CINEMA_OWNER");
        long totalVotesForConcert = concert.getConcertOwnerVotes().size();

        if (totalVotesForConcert >= totalConcertOwners) {
            concert.setStatusConcert("END");
            concertRepository.save(concert);
        }   
    }
     public List<Concert> searchConcerts(String keyword) {
        return concertRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    @Override
    public void updateAverageRating(Integer concertID) {
        List<Rating> ratings = ratingRepository.findByConcert_ConcertID(concertID);
        double averageRating = ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
        Concert concert = getConcertById(concertID);
        if (concert != null) {
            concert.setAverageRating(averageRating);
            concert.setRatingCount(ratings.size());
            concertRepository.save(concert);
        }
    }
    public boolean isDuplicateTitle(String translatedTitle) {
        List<Concert> concerts = concertRepository.findAll();
        for (Concert concert : concerts) {
            if (translatedTitle.equalsIgnoreCase(concert.getTitle())) {
                return true; // Trả về true nếu tìm thấy sự trùng lặp
            }
        }
        return false; // Trả về false nếu không tìm thấy sự trùng lặp
    }
    public boolean concertExistsByTitle(String title) {
        return concertRepository.existsByTitle(title);
    }
    @Override
    public List<Concert> getAllComingSoonConcerts() {
        return concertRepository.findByStatusConcertAndAddressNotNull(Concert.StatusConcert.COMING_SOON.getStatus());
    }
    public List<Concert> getConcertsByGenre(String genre) {
        return concertRepository.findByGenre(genre); 
    }
    public List<Concert> findByConcertOwnerID(int yardId) {
        return concertRepository.findByConcertOwnerID(yardId);
    }
    
}