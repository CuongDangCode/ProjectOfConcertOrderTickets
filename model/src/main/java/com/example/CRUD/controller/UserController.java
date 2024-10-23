// package com.example.CRUD.controller;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.security.Principal;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.CRUD.service.ConcertService;
// import com.example.CRUD.service.NewsService;
// import com.example.CRUD.service.PromotionsService;
// import com.example.CRUD.service.TicketService;
// import com.example.CRUD.service.UserService;
// import com.example.CRUD.service.YardService;
// import com.example.mo.Concert;
// import com.example.mo.News;
// import com.example.mo.Promotions;
// import com.example.mo.Ticket;
// import com.example.mo.Users;
// import com.example.mo.Yard;

// import jakarta.servlet.http.HttpServletRequest;

// @Controller
// @RequestMapping("/user")
// public class UserController {

//     private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//     @Autowired
//     private UserService userService;
//     @Autowired
//     private TicketService ticketService;
//     @Autowired
//     private PromotionsService promotionsService;
//     @ModelAttribute
//     public void commonUser(Principal principal, Model model) {
//         if (principal != null) {
//             String email = principal.getName();
//             Users user = userService.getUsersByEmail(email);
//             model.addAttribute("user", user);
//         }
//     }

//     @GetMapping("/home")
//     public String showHomePage(Model model, Principal principal) {
//         Integer concertOwnerID = getConcertOwnerIDFromPrincipal(principal);
//         List<Concert> concerts = concertService.getAllConcerts();
//         List<Concert> simplifiedConcerts = concerts.stream()
//                 .map(m -> {
//                     Concert simplifiedConcert = new Concert();
//                     simplifiedConcert.setConcertID(m.getConcertID());
//                     simplifiedConcert.setTitle(m.getTitle());
//                     simplifiedConcert.setGenre(m.getGenre());
//                     simplifiedConcert.setAddress(m.getAddress());
//                     return simplifiedConcert;
//                 })
//                 .collect(Collectors.toList());
//         model.addAttribute("concerts", simplifiedConcerts);
//         List<Promotions> listPromotions = promotionsService.listAllByConcertOwnerID(concertOwnerID);
//         model.addAttribute("listPromotions", listPromotions);
//         List<Concert> comingSoonConcerts = concertService.getAllComingSoonConcerts();
//         model.addAttribute("comingSoonConcerts", comingSoonConcerts);
//         return "home";
//     }

//     @GetMapping("/profile")
//     public String getProfile(Model model, Principal principal) {
//         if (principal != null) {
//             String email = principal.getName();
//             Users user = userService.getUsersByEmail(email);
//             model.addAttribute("user", user);
//         }
//         return "profile";
//     }

//     @PostMapping("/upload-avatar")
//     public String changeAvatar(Model model, @RequestParam("file") MultipartFile file, Principal principal)
//             throws IOException {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         String originalFilename = file.getOriginalFilename();
//         Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/uploads", originalFilename);
//         Files.write(fileNameAndPath, file.getBytes());
//         user.setProfileImageURL(originalFilename);
//         userService.updateUser(user);
//         model.addAttribute("user", user);
//         return "redirect:/user/profile";
//     }

//     @GetMapping("/update-profile")
//     public String showUpdateProfile(Model model, Principal principal) {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         model.addAttribute("user", user);
//         return "update-profile";
//     }

//     @PostMapping("/update-profile")
//     public String updateProfile(Users updatedUser, Principal principal) {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         if (user != null) {
//             user.setUserName(updatedUser.getUserName());
//             user.setEmail(updatedUser.getEmail());
//             user.setPhone(updatedUser.getPhone());
//             user.setLocation(updatedUser.getLocation());
//             user.setBirthdate(updatedUser.getBirthdate());
//             userService.updateUser(user);
//         }
//         return "redirect:/user/profile";
//     }

//     @GetMapping("/change-password")
//     public String showChangePasswordForm(Model model, Principal principal) {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         model.addAttribute("user", user);
//         return "change-password";
//     }

//     @PostMapping("/change-password")
//     public String changePassword(@RequestParam("password") String password,
//                                  Principal principal,
//                                  Model model) {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         userService.updatePassword(user, password);
//         model.addAttribute("message", "Password changed successfully.");
//         return "change-password";
//     }

//     // @GetMapping("/member-points")
//     // public String showMemberPoints(Model model, Principal principal) {
//     //     if (principal != null) {
//     //         String email = principal.getName();
//     //         Users user = userService.getUsersByEmail(email);
//     //         if (user != null) {
//     //             model.addAttribute("points", user.getMemberPoints());
//     //         } else {
//     //             model.addAttribute("error", "User not found.");
//     //         }
//     //     }
//     //     return "member-points";
//     // }

//         @GetMapping("/mytickets")
//     public String ShowMyTickets(Model model, Principal principal) {
//         String email = principal.getName();
//         Users user = userService.getUsersByEmail(email);
//         int userId = user.getUserId();
//         List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
//         model.addAttribute("tickets", tickets);
//         return "mytickets";
//     }
//     @Autowired
//     private YardService service;

//     @Autowired
//     private PromotionsService promotionService;
//     @Autowired
//     private ConcertService concertService;
//     @Autowired
//     private NewsService newsService;

//     @GetMapping("/yards")
//     public String getYardsAndPromotions(
//             @RequestParam(value = "keyword", required = false) String keyword,
//             @RequestParam(value = "location", required = false) String location,
//             Model model) {

//         List<Yard> yards;

//         // Search by both keyword and location
//         if (keyword != null && !keyword.isEmpty() && location != null && !location.isEmpty()) {
//             yards = service.searchYards(keyword);
//         }
//         // Search by keyword only
//         else if (keyword != null && !keyword.isEmpty()) {
//             yards = service.searchYards(keyword);
//             model.addAttribute("keyword", keyword);
//         }
//         // Search by location only
//         else if (location != null && !location.isEmpty()) {
//             yards = service.searchYardsByLocation(location);
//         }
//         // Fetch all Yards if no search criteria provided
//         else {
//             yards = service.getAllYards();
//         }

//         model.addAttribute("yards", yards);

//         // Fetch promotions
//         List<Promotions> promotions = promotionService.listAll();
//         model.addAttribute("promotions", promotions);

//         // Fetch concerts (adjust as per your service method)
//         List<Concert> concerts = concertService.getAllConcerts();
//         model.addAttribute("concerts", concerts);

//         return "yards"; // Thymeleaf template name
//     }

//     @GetMapping("/promotionDetail/{id}")
//     public String showPromotionDetail(@PathVariable("id") Integer id, Model model) {
//         Promotions promotion = promotionService.getPromotionById(id);
//         if (promotion != null) {
//             model.addAttribute("promotion", promotion);
//             return "/promotion_detail";
//         } else {
//             return "redirect:/promotions";
//         }
//     }

//     @GetMapping("/newlist")
//     public String showNewList(Model model, Principal principal) {
//         // Extract concert owner ID from principal
//         Integer concertOwnerID = getConcertOwnerIDFromPrincipal(principal);

//         // Fetch news related to the concert owner
//         List<News> listNews = newsService.listAllByConcertOwnerID(concertOwnerID);

//         // Fetch all concerts (adjust as per your service method)
//         List<Concert> concerts = concertService.getAllConcerts();

//         // Fetch promotions for the concert owner
//         List<Promotions> promotions = promotionService.listAllByConcertOwnerID(concertOwnerID);

//         // Add fetched data to the model
//         model.addAttribute("listNews", listNews);
//         model.addAttribute("concerts", concerts);
//         model.addAttribute("promotions", promotions);

//         // Return the view name
//         return "newlist";
//     }
//     private Integer getConcertOwnerIDFromPrincipal(Principal principal) {
//         Users user = userService.getUsersByEmail(principal.getName());
//         if (user == null) {
//             throw new RuntimeException("User not found");
//         }
//         return user.getUserId();
//     }

//     public static class Utility {
//         public static String getSiteURL(HttpServletRequest request) {
//             String siteURL = request.getRequestURL().toString();
//             return siteURL.replace(request.getServletPath(), "");
//         }
//     }
// }
package com.example.CRUD.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.NewsService;
import com.example.CRUD.service.PromotionsService;
import com.example.CRUD.service.TicketService;
import com.example.CRUD.service.UserService;
import com.example.CRUD.service.YardService;
import com.example.mo.Concert;
import com.example.mo.News;
import com.example.mo.Promotions;
import com.example.mo.Ticket;
import com.example.mo.Users;
import com.example.mo.Yard;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private PromotionsService promotionsService;
    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            Users user = userService.getUsersByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        Integer concertOwnerID = getConcertOwnerIDFromPrincipal(principal);
        List<Concert> concerts = concertService.getAllConcerts();
        List<Concert> simplifiedConcerts = concerts.stream()
                .map(m -> {
                    Concert simplifiedConcert = new Concert();
                    simplifiedConcert.setConcertID(m.getConcertID());
                    simplifiedConcert.setTitle(m.getTitle());
                    simplifiedConcert.setGenre(m.getGenre());
                    simplifiedConcert.setAddress(m.getAddress());
                    return simplifiedConcert;
                })
                .collect(Collectors.toList());
        model.addAttribute("concerts", simplifiedConcerts);
        List<Promotions> listPromotions = promotionsService.listAllByConcertOwnerID(concertOwnerID);
        model.addAttribute("listPromotions", listPromotions);
        List<Concert> comingSoonConcerts = concertService.getAllComingSoonConcerts();
        model.addAttribute("comingSoonConcerts", comingSoonConcerts);
        return "home";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Users user = userService.getUsersByEmail(email);
            model.addAttribute("user", user);
        }
        return "profile";
    }

    @PostMapping("/upload-avatar")
public String changeAvatar(Model model, @RequestParam("file") MultipartFile file, Principal principal) {
    try {
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        String originalFilename = file.getOriginalFilename();
        Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "/uploads", originalFilename);
        
        // Đảm bảo thư mục uploads tồn tại
        Files.createDirectories(fileNameAndPath.getParent());
        
        Files.write(fileNameAndPath, file.getBytes());
        user.setProfileImageURL(originalFilename);
        userService.updateUser(user);
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
        Users user = userService.getUsersByEmail(email);
        model.addAttribute("user", user);
        return "update-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(Users updatedUser, Principal principal) {
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        if (user != null) {
            user.setUserName(updatedUser.getUserName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setLocation(updatedUser.getLocation());
            user.setBirthdate(updatedUser.getBirthdate());
            userService.updateUser(user);
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, Principal principal) {
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        model.addAttribute("user", user);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String password,
                                 Principal principal,
                                 Model model) {
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        userService.updatePassword(user, password);
        model.addAttribute("message", "Password changed successfully.");
        return "change-password";
    }

    // @GetMapping("/member-points")
    // public String showMemberPoints(Model model, Principal principal) {
    //     if (principal != null) {
    //         String email = principal.getName();
    //         Users user = userService.getUsersByEmail(email);
    //         if (user != null) {
    //             model.addAttribute("points", user.getMemberPoints());
    //         } else {
    //             model.addAttribute("error", "User not found.");
    //         }
    //     }
    //     return "member-points";
    // }

        @GetMapping("/mytickets")
    public String ShowMyTickets(Model model, Principal principal) {
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);
        int userId = user.getUserId();
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        model.addAttribute("tickets", tickets);
        return "mytickets";
    }
    @Autowired
    private YardService service;

    @Autowired
    private PromotionsService promotionService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private NewsService newsService;

    @GetMapping("/yards")
    public String getYardsAndPromotions(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "location", required = false) String location,
            Model model) {

        List<Yard> yards;

        // Search by both keyword and location
        if (keyword != null && !keyword.isEmpty() && location != null && !location.isEmpty()) {
            yards = service.searchYards(keyword);
        }
        // Search by keyword only
        else if (keyword != null && !keyword.isEmpty()) {
            yards = service.searchYards(keyword);
            model.addAttribute("keyword", keyword);
        }
        // Search by location only
        else if (location != null && !location.isEmpty()) {
            yards = service.searchYardsByLocation(location);
        }
        // Fetch all Yards if no search criteria provided
        else {
            yards = service.getAllYards();
        }

        model.addAttribute("yards", yards);

        // Fetch promotions
        List<Promotions> promotions = promotionService.listAll();
        model.addAttribute("promotions", promotions);

        // Fetch concerts (adjust as per your service method)
        List<Concert> concerts = concertService.getAllConcerts();
        model.addAttribute("concerts", concerts);

        return "yards"; // Thymeleaf template name
    }

    @GetMapping("/promotionDetail/{id}")
    public String showPromotionDetail(@PathVariable("id") Integer id, Model model) {
        Promotions promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            model.addAttribute("promotion", promotion);
            return "/promotion_detail";
        } else {
            return "redirect:/promotions";
        }
    }

    @GetMapping("/newlist")
    public String showNewList(Model model, Principal principal) {
        // Extract concert owner ID from principal
        Integer concertOwnerID = getConcertOwnerIDFromPrincipal(principal);

        // Fetch news related to the concert owner
        List<News> listNews = newsService.listAllByConcertOwnerID(concertOwnerID);

        // Fetch all concerts (adjust as per your service method)
        List<Concert> concerts = concertService.getAllConcerts();

        // Fetch promotions for the concert owner
        List<Promotions> promotions = promotionService.listAllByConcertOwnerID(concertOwnerID);

        // Add fetched data to the model
        model.addAttribute("listNews", listNews);
        model.addAttribute("concerts", concerts);
        model.addAttribute("promotions", promotions);

        // Return the view name
        return "newlist";
    }
    private Integer getConcertOwnerIDFromPrincipal(Principal principal) {
        Users user = userService.getUsersByEmail(principal.getName());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getUserId();
    }

    public static class Utility {
        public static String getSiteURL(HttpServletRequest request) {
            String siteURL = request.getRequestURL().toString();
            return siteURL.replace(request.getServletPath(), "");
        }
    }
}
