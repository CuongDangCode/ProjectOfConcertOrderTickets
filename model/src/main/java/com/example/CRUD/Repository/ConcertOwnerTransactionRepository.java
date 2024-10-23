package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mo.ConcertOwnerTransaction;

@Repository
public interface ConcertOwnerTransactionRepository extends JpaRepository<ConcertOwnerTransaction, Integer> {
    List<ConcertOwnerTransaction> findByConcertOwnerConcertOwnerID(int concertOwnerID);
}
