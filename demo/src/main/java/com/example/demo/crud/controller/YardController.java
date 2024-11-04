package com.example.CRUD.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

import com.example.CRUD.service.AreaService;
import com.example.CRUD.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CRUD.config.FileUploadUtil;
import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.YardService;
import com.example.mo.Concert;
import com.example.mo.Yard;
import com.example.mo.Area;

@Controller
public class YardController {

    @Autowired
    private YardService service;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private FileService fileService;

    @GetMapping("/yard")
    public String viewYardPage(Model model) {
        List<Yard> listYards = service.listAll();
        model.addAttribute("listYards", listYards);
        return "yard";
    }

    @GetMapping("/yard/new")
    public String showNewForm(Model model) {
        List<Concert> concerts = concertService.getAllComingSoonConcerts();
        model.addAttribute("yard", new Yard());
        model.addAttribute("concerts", concerts);
        model.addAttribute("pageTitle", "Add New Yard");
        return "yard_form";
    }

    @PostMapping("/yard/save")
    public String saveYard(
            @ModelAttribute("yard") Yard yard,
            @RequestParam(value = "photoYardFile", required = false) MultipartFile multipartFile,
            @RequestParam("concertID")String[] checkboxValue,
            RedirectAttributes ra, Principal principal) {
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String image = fileService.uploadFile(multipartFile);
                yard.setPhotoYard(image);
            }
            Set<Concert> concerts = new HashSet<>();
            for (String checkbox : checkboxValue) {
                Concert concert = concertService.getConcertById(Integer.parseInt(checkbox));
                concerts.add(concert);
            }
            yard.setConcerts(concerts);
            service.save(yard);
            ra.addFlashAttribute("message", "The yard has been saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Failed to save the yard due to an error: " + e.getMessage());
        }
        return "redirect:/yard";
    }
//@PostMapping("/yard/save")
//public String saveYard(
//        @ModelAttribute("yard") Yard yard,
//        @RequestParam(value = "photoYardFile", required = false) MultipartFile multipartFile,
//        RedirectAttributes ra, Principal principal) {
//    try {
//        // Xử lý file upload nếu có
//        if (multipartFile != null && !multipartFile.isEmpty()) {
//            String image = fileService.uploadFile(multipartFile);
//            yard.setPhotoYard(image);
//        }
//
//        // Lưu yard mà không cần concertID
//        service.save(yard);
//        ra.addFlashAttribute("message", "The yard has been saved successfully.");
//    } catch (IOException e) {
//        ra.addFlashAttribute("error", "Failed to save the yard: " + e.getMessage());
//    }
//    return "redirect:/yard";
//}

//    @GetMapping("/yard/edit/{id}")
//    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
//        try {
//            Yard yard = service.get(id);
//            model.addAttribute("yard", yard);
//            model.addAttribute("pageTitle", "Edit Yard (ID: " + id + ")");
//            return "yard_form";
//        } catch (YardNotFoundException e) {
//            ra.addFlashAttribute("error", "Yard not found");
//            return "redirect:/yard";
//        }
//    }
@GetMapping("/yard/edit/{id}")
public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
    try {
        Yard yard = service.get(id);

        // Fetch all available concerts
        List<Concert> allConcerts = concertService.getAllConcerts();

        // Get the concerts currently associated with the yard
        Set<Concert> selectedConcerts = yard.getConcerts();

        // Prepare concert list with selected concerts marked
        model.addAttribute("allConcerts", allConcerts);
        model.addAttribute("selectedConcerts", selectedConcerts);
        model.addAttribute("yard", yard);
        model.addAttribute("pageTitle", "Edit Yard (ID: " + id + ")");

        return "yard_form";
    } catch (YardNotFoundException e) {
        ra.addFlashAttribute("error", "Yard not found");
        return "redirect:/yard";
    }
}

    @GetMapping("/yard/delete/{id}")
    public String deleteYard(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "The Yard has been deleted successfully.");
        } catch (YardNotFoundException e) {
            ra.addFlashAttribute("error", "Failed to delete the Yard.");
        }
        return "redirect:/yard";
    }

    private Integer getConcertOwnerIDFromPrincipal(Principal principal) {
        // Implement method to get concertOwnerID from Principal
        return 1; // Temporary value for demonstration
    }

    @GetMapping("/yards")
    public String showYardsByConcert(@RequestParam("concertId") Integer concertId, Model model) {
        List<Yard> yards = service.getYardsByConcertID(concertId);
        model.addAttribute("yards", yards);
        model.addAttribute("concertId", concertId);
        return "yard_selection"; // Trả về trang yard_selection.html
    }

    @GetMapping("/areas")
    public String showAreasByYard(@RequestParam("yardId") Integer yardId, Model model) {
        List<Area> areas = areaService.getAreasByYardId(yardId);
        model.addAttribute("areas", areas);
        model.addAttribute("yardId", yardId);
        return "area_selection"; // Trả về trang area_selection.html
    }
}