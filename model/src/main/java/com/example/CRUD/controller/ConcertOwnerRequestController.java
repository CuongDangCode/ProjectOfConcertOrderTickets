package com.example.CRUD.controller;

    
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.CRUD.service.ConcertOwnerRequestService;
import com.example.CRUD.service.UserService;
import com.example.mo.ConcertOwnerRequest;
import com.example.mo.Users;

@Controller
public class ConcertOwnerRequestController {
    @Autowired
    private final ConcertOwnerRequestService concertOwnerRequestService;
    private final UserService userService;


    public ConcertOwnerRequestController(UserService userService, ConcertOwnerRequestService concertOwnerRequestService) {
        this.userService = userService;
        this.concertOwnerRequestService = concertOwnerRequestService;
    }
    @GetMapping("/register-concertowner")
    public String showRegistrationPage() {
        return "register-concert-owner";
    }
    
    @PostMapping("/register-concertowner/save")
public String createConcertOwnerRequest(@ModelAttribute("concertOwnerRequest") ConcertOwnerRequest concertOwnerRequest, Principal principal, Model model) {
    String email = principal.getName();
    Users currentUser = userService.getUsersByEmail(email);
    if (currentUser.getRole().equals("CONCERT_OWNER")) {
        model.addAttribute("errorMessage", "You are already a Concert Owner. You cannot register again.");
        return "register-concert-owner";
    }
    if (concertOwnerRequestService.isUserRegisteredAsConcertOwner(currentUser.getUserId())) {
        model.addAttribute("errorMessage", "You have already registered as a Concert Owner. Please wait for the admin to review and approve your request.");
        return "register-concert-owner";
    }
    concertOwnerRequest.setUsers(currentUser);
    concertOwnerRequestService.saveConcertOwnerRequest(concertOwnerRequest);
    model.addAttribute("successMessage", "Your Concert Owner request has been successfully submitted.");
    return "register-concert-owner";
}

    @GetMapping("/show-concert-owner-request")
    public String getAllConcertOwnerRequests(Model model) {
        List<ConcertOwnerRequest> concertOwnerRequests = concertOwnerRequestService.getAllConcertOwnerRequests();
        model.addAttribute("concertOwnerRequests", concertOwnerRequests);
        return "concert-owner-request";
    }
    @PostMapping("/{id}/approve")
    public String approveConcertOwnerRequest(@PathVariable int id) {
        concertOwnerRequestService.approveConcertOwnerRequest(id);

        return "redirect:/show-concert-owner-request";
    }

    @PostMapping("/{id}/reject")
    public String rejectConcertOwnerRequest(@PathVariable int id) {
        concertOwnerRequestService.rejectConcertOwnerRequest(id);
        return "redirect:/show-concert-owner-request";
    }
}
