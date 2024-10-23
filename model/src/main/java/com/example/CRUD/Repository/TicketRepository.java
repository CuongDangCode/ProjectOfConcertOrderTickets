package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mo.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByUserUserId(int userId);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.concert m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :concertTitle, '%'))")
    List<Ticket> findTicketsByConcertTitleContainingIgnoreCase(@Param("concertTitle") String concertTitle);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.concert")
    List<Ticket> findAllTicketsWithConcerts();
}
