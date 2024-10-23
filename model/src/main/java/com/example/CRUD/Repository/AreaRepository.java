package com.example.CRUD.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mo.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
       @Query("SELECT new Area(r.areaID, r.roomname) FROM Area r JOIN Showtime s ON r.areaID = s.area.areaID " +
       "WHERE s.yard.yardID = :yardId AND s.concert.concertID = :concertId AND s.showTime = :showTime AND s.showDate = :showDate AND s.showtimeID = :showTimeId")
       List<Area> findAreasByYardIdAndConcertIdAndShowTimeAndShowDate(@Param("yardId") int yardId,
                                                                                         @Param("concertId") int concertId,
                                                                                         @Param("showTime") Time showTime,
                                                                                         @Param("showDate") Date showDate,
                                                                                         @Param("showTimeId") int showTimeId);
}
