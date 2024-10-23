package com.example.CRUD.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

@Controller
public class YardController {

    @Autowired
    private YardService service;
    @Autowired
    private ConcertService concertService;

    @GetMapping("/yard")
    public String viewYardPage(Model model) {
        List<Yard> listYards = service.listAll();
        model.addAttribute("listYards", listYards);
        return "yard";
    }

    @GetMapping("/yard/new")
    public String showNewForm(Model model) {
        model.addAttribute("yard", new Yard());
        model.addAttribute("pageTitle", "Add New Yard");
        return "yard_form";
    }

    @PostMapping("/yard/save")
    public String saveYard(
            @ModelAttribute("yard") Yard yard,
            @RequestParam(value = "photoYardFile", required = false) MultipartFile multipartFile,
            @RequestParam("concertID") String concertIDs, // Add this parameter to capture the concert IDs
            RedirectAttributes ra, Principal principal) {

        try {
            // Parse concert IDs and associate with the yard
            Set<Concert> concerts = new HashSet<>();
            for (String concertID : concertIDs.split(";")) {
                Concert concert = concertService.getConcertById(Integer.parseInt(concertID.trim()));
                if (concert != null) {
                    concerts.add(concert);
                }
            }
            yard.setConcerts(concerts);

            // Handle file upload
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                yard.setPhotoYard(fileName);
                Yard savedYard = service.save(yard);
                String uploadDir = "yard-photo/" + savedYard.getYardID();
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } else {
                service.save(yard); // Save yard if no file is uploaded
            }

            ra.addFlashAttribute("message", "The yard has been saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Failed to save the yard due to an error: " + e.getMessage());
        }
        return "redirect:/yard";
    }

    @GetMapping("/yard/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Yard yard = service.get(id);
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
}