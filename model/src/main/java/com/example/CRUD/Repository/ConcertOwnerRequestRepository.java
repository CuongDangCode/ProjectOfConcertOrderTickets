package com.example.CRUD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mo.ConcertOwnerRequest;

@Repository
public interface ConcertOwnerRequestRepository extends JpaRepository<ConcertOwnerRequest, Integer> {
    ConcertOwnerRequest findByUsersUserId(int userId);
}