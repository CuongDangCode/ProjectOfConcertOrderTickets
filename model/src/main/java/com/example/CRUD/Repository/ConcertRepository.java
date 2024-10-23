package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mo.Concert;


@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    List<Concert> findByTitleContainingIgnoreCase(String keyword);
    boolean existsByTitle(String title);
    List<Concert> findByStatusConcertAndAddressNotNull(String statusConcert);
    List<Concert> findByGenre(String genre);
    List<Concert> findByConcertOwnerID(Integer concertOwnerID);
}