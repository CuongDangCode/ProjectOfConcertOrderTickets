package com.example.CRUD.Repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mo.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    
    List<Showtime> findByConcertID(Integer concertID);
    
    List<Showtime> findByShowDate(Date showDate);
    
    List<Showtime> findByConcertOwnerID(Integer concertOwnerID);
    
    List<Showtime> findByConcertIDAndConcertOwnerID(Integer concertID, Integer concertOwnerID);

    @Query("SELECT s FROM Showtime s WHERE s.concert.concertID = :concertID")
    List<Showtime> findByConcert_ConcertID(@Param("concertID") Integer concertID);

    @Query(value = "SELECT s.show_date, s.show_time, s.showtime_id FROM Showtime s JOIN Yard t ON s.yard_id = t.yard_id WHERE s.concert_id = :concertID AND t.yard_id = :yardID", nativeQuery = true)
    List<Object[]> findByConcertIDAndYardID(@Param("concertID") Integer concertID, @Param("yardID") Integer yardID);

    @Query("SELECT s.showDate FROM Showtime s WHERE s.showtimeID = :showtimeId")
    Date findShowDateByShowtimeId(@Param("showtimeId") Integer showtimeId);

    @Query("SELECT s FROM Showtime s WHERE s.concertOwnerID = :concertOwnerID")
    Page<Showtime> findByConcertOwnerID(@Param("concertOwnerID") Integer concertOwnerID, Pageable pageable);

    @Query("SELECT s FROM Showtime s WHERE s.concert.concertID = :concertID AND s.concertOwnerID = :concertOwnerID")
    Page<Showtime> findByConcertIDAndConcertOwnerID(@Param("concertID") Integer concertID, @Param("concertOwnerID") Integer concertOwnerID, Pageable pageable);

    @Query("SELECT s FROM Showtime s WHERE s.showDate = :showDate")
    Page<Showtime> findByShowDate(@Param("showDate") Date showDate, Pageable pageable);
}
