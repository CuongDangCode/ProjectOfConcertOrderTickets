package com.example.CRUD.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.NewsService;
import com.example.CRUD.service.PromotionsService;
import com.example.CRUD.service.YardService;
import com.example.mo.Concert;
import com.example.mo.News;
import com.example.mo.Promotions;
import com.example.mo.Yard;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private PromotionsService promotionService;

    @Autowired
    private YardService yardService;

    @GetMapping("/")
    public String showHome(Model model) {
        List<Concert> concerts = concertService.getAllConcerts();
        model.addAttribute("concerts", concerts);
        return "home_Guest"; // This should match your Thymeleaf template name
    }

    @GetMapping("/newlist_guest")
    public String showNew(Model model) {
        List<News> listNews = newsService.getAllNews();
        List<Concert> concerts = concertService.getAllConcerts();
        List<Promotions> promotions = promotionService.listAll();
        model.addAttribute("promotions", promotions);
        model.addAttribute("listNews", listNews);
        model.addAttribute("concerts", concerts);
        return "newlist_guest";
    }

    @GetMapping("/yards_Guest")
    public String getYards(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "location", required = false) String location,
            Model model) {

        List<Yard> yards;
        if (keyword != null && !keyword.isEmpty() && location != null && !location.isEmpty()) {
            yards = yardService.searchYards(keyword);
        } else if (keyword != null && !keyword.isEmpty()) {
            yards = yardService.searchYards(keyword);
            model.addAttribute("keyword", keyword);
        } else if (location != null && !location.isEmpty()) {
            yards = yardService.searchYardsByLocation(location);
        } else {
            yards = yardService.getAllYards();
        }

        model.addAttribute("yards", yards);
        List<Promotions> promotions = promotionService.listAll();
        model.addAttribute("promotions", promotions);
        List<Concert> concerts = concertService.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "yards_Guest";
    }

    @GetMapping("/promotionDetailGuest/{id}")
    public String showPromotionDetailGuest(@PathVariable("id") Integer id, Model model) {
        Promotions promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            model.addAttribute("promotion", promotion);
            return "promotion_detail_guest";
        } else {
            return "redirect:/yards_Guest";
        }
    }
}
