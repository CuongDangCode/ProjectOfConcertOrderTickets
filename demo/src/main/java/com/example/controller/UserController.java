package com.example.crud.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.crud.service.UserService;
import com.example.mo.Users;

@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService ser;

    @ModelAttribute
    public void commonUser(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            Users user = ser.getUsersByEmail(email);
            m.addAttribute("user", user);
        }
    }

    @GetMapping("/home")
    public String profile() {
        return "home";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    @PostMapping("/upload-avatar")
    public String changeAvatar(Model model, @RequestParam("file") MultipartFile file, Principal principal) {
        try {
            String email = principal.getName();
            Users user = ser.getUsersByEmail(email);
            String originalFilename = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/uploads", originalFilename);
            
            // Đảm bảo thư mục uploads tồn tại
            Files.createDirectories(fileNameAndPath.getParent());
            
            Files.write(fileNameAndPath, file.getBytes());
            user.setProfileImageURL(originalFilename);
            ser.updateUser(user);
            model.addAttribute("user", user);
        } catch (IOException e) {
            // Xử lý ngoại lệ (ví dụ: ghi log, đặt thông báo lỗi trong model, v.v.)
            model.addAttribute("error", "Không thể tải lên ảnh đại diện: " + e.getMessage());
            return "redirect:/user/profile"; // Chuyển hướng với lỗi
        }
        return "redirect:/user/profile";
    }

@GetMapping("/update-profile")
    public String showUpdateProfile(Model model, Principal principal) {
        String email = principal.getName();
        Users user = ser.getUsersByEmail(email);
        model.addAttribute("user", user);
        return "update-profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, Principal principal) {
        String email = principal.getName();
        Users user = ser.getUsersByEmail(email);
        model.addAttribute("user", user);
        return "change-password";
    }
}