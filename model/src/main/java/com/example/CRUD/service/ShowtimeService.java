package com.example.CRUD.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.AreaRepository;
import com.example.CRUD.Repository.ShowtimeRepository;
import com.example.CRUD.controller.ShowtimeNotFoundException;
import com.example.mo.Area;
import com.example.mo.Showtime;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository repo;

    @Autowired
    private AreaRepository areaRepository;

    public Date getShowDate(Integer showtimeId) {
        return repo.findShowDateByShowtimeId(showtimeId);
    }

    public Time getShowtimeNameById(int showtimeId) {
        Showtime showtime = repo.findById(showtimeId)
                .orElseThrow(() -> new EntityNotFoundException("Showtime not found"));
        return showtime.getShowTime();
    }

    public List<Showtime> getShowtimesByConcertID(Integer concertID) {
        List<Showtime> showtimes = repo.findByConcert_ConcertID(concertID);
        System.out.println("Fetched showtimes for concert ID " + concertID + ": " + showtimes);
        return showtimes;
    }

    public List<Object[]> getShowtimesByConcertIDAndYardID(Integer concertID, Integer yardID) {
        return repo.findByConcertIDAndYardID(concertID, yardID);
    }

    public Page<Showtime> getShowtimesByConcertIDAndConcertOwnerID(Integer concertID, Integer concertOwnerID, Pageable pageable) {
        return repo.findByConcertIDAndConcertOwnerID(concertID, concertOwnerID, pageable);
    }

    public Page<Showtime> getShowtimesByDate(Date showDate, Pageable pageable) {
        return repo.findByShowDate(showDate, pageable);
    }

    public Page<Showtime> listAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Showtime> listAllByConcertOwnerID(Integer concertOwnerID, Pageable pageable) {
        return repo.findByConcertOwnerID(concertOwnerID, pageable);
    }

    public void save(Showtime showtime) {
        repo.save(showtime);
    }

    public Integer getYardIdByAreaId(Integer areaId) {
        Optional<Area> areaOptional = areaRepository.findById(areaId);
        if (areaOptional.isPresent()) {
            return areaOptional.get().getYard().getYardID();
        }
        return null;
    }

    public Showtime get(Integer showtimeID) throws ShowtimeNotFoundException {
        Optional<Showtime> result = repo.findById(showtimeID);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ShowtimeNotFoundException("Could not find any showtime with ID " + showtimeID);
    }

    public void delete(Integer showtimeID) throws ShowtimeNotFoundException {
        if (!repo.existsById(showtimeID)) {
            throw new ShowtimeNotFoundException("Could not find any showtime with ID " + showtimeID);
        }
        repo.deleteById(showtimeID);
    }
}
