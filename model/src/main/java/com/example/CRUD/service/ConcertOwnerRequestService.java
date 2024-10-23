package com.example.CRUD.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.example.CRUD.Repository.ConcertOwnerRepository;
import com.example.CRUD.Repository.ConcertOwnerRequestRepository;
import com.example.CRUD.Repository.UserRepository;
import com.example.mo.ConcertOwner;
import com.example.mo.ConcertOwnerRequest;
import com.example.mo.Users;

@Service
public class ConcertOwnerRequestService {
    private ConcertOwnerRequestRepository concertOwnerRequestRepository;
    // private ConcertOwnerRepository concertOwnerRepository;
    private UserRepository userRepository;

    @Autowired
    public void setConcertOwnerRequestRepository(ConcertOwnerRequestRepository concertOwnerRequestRepository, UserRepository userRepository) {
        this.concertOwnerRequestRepository = concertOwnerRequestRepository;
        this.userRepository = userRepository;
    }

    // @Autowired
    // public void setConcertOwnerRepository(ConcertOwnerRepository concertOwnerRepository) {
    //     this.concertOwnerRepository = concertOwnerRepository;
    // }

    public ConcertOwnerRequest saveConcertOwnerRequest(ConcertOwnerRequest concertOwnerRequest) {
        return concertOwnerRequestRepository.save(concertOwnerRequest);
    }

    public List<ConcertOwnerRequest> getAllConcertOwnerRequests() {
        return concertOwnerRequestRepository.findAll();
    }

    public void approveConcertOwnerRequest(int concertOwnerRequestId) {
        ConcertOwnerRequest request = concertOwnerRequestRepository.findById(concertOwnerRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Concert owner request not found"));
        Users user = request.getUsers();
        user.setRole("Concert_OWNER");
        userRepository.save(user);
        ConcertOwner concertOwner = new ConcertOwner();
        concertOwner.setUsers(request.getUsers());
        // concertOwner.setCinemaName(request.getCinemaName());
        // concertOwner.setAddressCinema(request.getAddressCinema());
        concertOwner.setHotline(request.getHotline());
        concertOwner.setEmail(request.getEmail());
        // concertOwner.setEmployeeID(request.getEmployeeID());

        // concertOwnerRepository.save(concertOwner);
        concertOwnerRequestRepository.delete(request);
    }

    public void rejectConcertOwnerRequest(int concertOwnerRequestId) {
        concertOwnerRequestRepository.deleteById(concertOwnerRequestId);
    }

    public boolean isUserRegisteredAsConcertOwner(int userId) {
        ConcertOwnerRequest concertOwnerRequest = concertOwnerRequestRepository.findByUsersUserId(userId);
        return concertOwnerRequest != null;
    }
    
}