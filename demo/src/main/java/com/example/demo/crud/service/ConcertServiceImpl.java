package com.example.crud.service;

import com.example.crud.repository.ConcertRepository;
import com.example.mo.Concert;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @Override
    public Concert getConcertById(Integer concertID) {
        return concertRepository.findById(concertID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Concert not found"));
    }

    @Override
    public Concert saveConcert(Concert concert) {
        // Kiểm tra các trường cần thiết ở đây
        return concertRepository.save(concert);
    }

    @Override
    public void deleteConcert(Integer id) {
        if (!concertRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Concert not found");
        }
        concertRepository.deleteById(id);
    }
}
