package com.example.CRUD.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CRUD.Repository.ConcertOwnerTransactionRepository;
import com.example.mo.ConcertOwnerTransaction;

@Service
public class ConcertOwnerTransactionService {

    @Autowired
    private ConcertOwnerTransactionRepository repository;

    public List<ConcertOwnerTransaction> getTransactionsByConcertOwnerId(int concertOwnerId) {
        return repository.findByConcertOwnerConcertOwnerID(concertOwnerId);
    }

    public ConcertOwnerTransaction saveTransaction(ConcertOwnerTransaction transaction) {
        return repository.save(transaction);
    }
}
