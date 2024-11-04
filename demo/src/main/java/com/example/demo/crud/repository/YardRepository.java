package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mo.Yard;

@Repository
public interface YardRepository extends JpaRepository<Yard, Integer> {
    List<Yard> findByYardOwnerID(Integer yardOwnerID);

    @Query("SELECT t.yardOwnerID FROM Yard t WHERE t.yardID = :yardID")
    Integer findConcertOwnerIdByYardId(@Param("yardID") Integer yardID);

    List<Yard> findByYardNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String yardName,
            String address);

    List<Yard> findByAddressContainingIgnoreCase(String address);

    @Query("SELECT t FROM Yard t JOIN t.concerts m WHERE m.concertID = :concertID")
    List<Yard> findYardsByConcertID(@Param("concertID") Integer concertID);

    @Query("SELECT y FROM Yard y JOIN y.concerts c WHERE c.concertID = :concertId")
    List<Yard> findByConcertId(@Param("concertId") Integer concertId);
}
