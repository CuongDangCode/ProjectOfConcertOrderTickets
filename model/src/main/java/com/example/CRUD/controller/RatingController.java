package com.example.CRUD.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.RatingService;
import com.example.CRUD.service.UserService;
import com.example.mo.Concert;
import com.example.mo.Rating;
import com.example.mo.Users;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    private final ConcertService concertService;
    private final RatingService ratingService;
    private final UserService userService;

    @Autowired
    public RatingController(ConcertService concertService, RatingService ratingService, UserService userService) {
        this.concertService = concertService;
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Users user = userService.getUsersByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @PostMapping("/{concertID}")
    public String submitRating(@PathVariable Integer concertID,
                               @RequestParam("content") String content,
                               @RequestParam(value = "score", required = false) Integer score,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
    
        if (score == null) {
            redirectAttributes.addFlashAttribute("message", "Bạn hãy đánh giá điểm.");
            return "redirect:/concert/book/" + concertID;
        }
    
        if (content.length() > 255) {
            redirectAttributes.addFlashAttribute("message", "Nội dung đánh giá không được vượt quá 255 ký tự.");
            return "redirect:/concert/book/" + concertID;
        }
    
        List<String> badwords = Arrays.asList("Con Cac", "cc", "ngu","dcm","cl","cac","cong san","phong","Quan");
        for (String word : badwords) {
            if (content.toLowerCase().contains(word.toLowerCase())) {
                redirectAttributes.addFlashAttribute("message", "Nội dung đánh giá chứa từ cấm.");
                return "redirect:/concert/book/" + concertID;
            }
        }
    
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        Rating existingRating = ratingService.getRatingByUserAndConcert(user, concertID);
if (existingRating != null) {
            redirectAttributes.addFlashAttribute("message", "Bạn đã đánh giá cho bộ phim này rồi.");
        } else {
            Concert concert = concertService.getConcertById(concertID);
            if (concert == null) {
                return "redirect:/error";
            }
    
            Rating rating = new Rating();
            rating.setConcert(concert);
            rating.setUser(user);
            rating.setContent(content);
            rating.setScore(score);
    
            try {
                ratingService.saveRating(rating);
            } catch (DataIntegrityViolationException e) {
                redirectAttributes.addFlashAttribute("message", "Nội dung đánh giá không được vượt quá 255 ký tự.");
                return "redirect:/concert/book/" + concertID;
            }
        }
    
        return "redirect:/concert/book/" + concertID;
    }
    

    
   
    @PostMapping("/delete/{ratingId}")
    public String deleteRating(@PathVariable Integer ratingId,
                               Principal principal,
                               Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        Rating rating = ratingService.getRatingById(ratingId);

        if (rating != null && rating.getUser().equals(user)) {
            Integer concertId = rating.getConcert().getConcertID();
            ratingService.deleteRating(ratingId);
            concertService.updateAverageRating(concertId);
        } else {
            model.addAttribute("error", "You can only delete your own ratings.");
        }

        return "redirect:/concert/book/" + rating.getConcert().getConcertID();
    }
}