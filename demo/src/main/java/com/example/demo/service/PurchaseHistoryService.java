package com.example.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crud.repository.PurchaseHistoryRepository;
import com.example.mo.PurchaseHistory;
import com.example.mo.Users;

@Service
public class PurchaseHistoryService {
    @Autowired
    private PurchaseHistoryRepository historyRepository;

    @Autowired
    private UserByAdminService adminService;
    
    public List<PurchaseHistory> getPurchaseHistoryById(Integer UserId){
        Users user = adminService.findById(UserId);
        return historyRepository.findByUser(user);
    }   
}