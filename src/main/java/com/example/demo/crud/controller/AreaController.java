package com.example.CRUD.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.CRUD.service.AreaService;
import com.example.CRUD.service.SeatService;
import com.example.mo.Area;
import com.example.mo.Seat;
import com.example.CRUD.controller.AreaController;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.CRUD.service.AreaService;
import com.example.mo.Area;





@Controller
public class AreaController {
    @Autowired private AreaService service;
    @Autowired
    private SeatService seatService;
    @GetMapping("/area")
    public String showAreaList(Model model) {
        List<Area> listArea = service.listAll();
        model.addAttribute("listArea", listArea);
        return "area"; 
    }

    @GetMapping("/area/new")
    public String showNewForm(Model model) {
        model.addAttribute("area", new Area());
        model.addAttribute("pageTitle", "Add New Area");
        return "area_form";
    }

    @PostMapping("/area/save")
    public String saveArea(Area area, RedirectAttributes ra) {
        
        service.save(area);
        ra.addFlashAttribute("message", "The area has been saved successfully.");
        return "redirect:/area";
        
    }

     @GetMapping("area/edit/{AreaID}")
    public String showEditForm(@PathVariable("AreaID") Integer AreaID, Model model, RedirectAttributes ra) {
        try {
            Area area = service.get(AreaID);
            model.addAttribute("area", area);
            model.addAttribute("pageTitle", "Edit User (ID: " + AreaID + ")");
            return "area_form";
        } catch (AreaNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
             return "redirect:/area";
        }
        
        
    }


    @GetMapping("area/delete/{AreaID}")
    public String deleteShowtime(@PathVariable("AreaID") Integer AreaID, RedirectAttributes ra) {
        try {
            service.delete(AreaID);
            
        } catch (AreaNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/area";


//        @GetMapping("/seats")
//        public String showSeatsByArea(@RequestParam("AreaId") Integer areaId, Model model) {
//            List<Seat> seats = seatService.getSeatsByAreaId(areaId);
//            model.addAttribute("seats", seats);
//            model.addAttribute("areaId", areaId);
//            return "seat_selection"; // Trả về trang seat_selection.html
//        }
    }








}