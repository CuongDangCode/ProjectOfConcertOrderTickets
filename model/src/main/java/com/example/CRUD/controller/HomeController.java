package com.example.CRUD.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.CRUD.Repository.UserRepository;
import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.PromotionsService;
import com.example.CRUD.service.UserService;
import com.example.mo.Concert;
import com.example.mo.Promotions;
import com.example.mo.Users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.bytebuddy.utility.RandomString;

@Controller
public class HomeController {

    private final ConcertService concertService;
    private final PromotionsService promotionsService;
    private final UserService userService;
    private final UserRepository userRepo;

    @Autowired
    public HomeController(ConcertService concertService, PromotionsService promotionsService, UserService userService,
            UserRepository userRepo) {
        this.concertService = concertService;
        this.promotionsService = promotionsService;
        this.userService = userService;
        this.userRepo = userRepo;
    }
    @GetMapping("/home_Guest")
    public String showHomeGuestPage(Model model) {
        // Thêm logic nếu cần thiết, ví dụ: lấy danh sách phim, khuyến mãi, v.v.
        return "home_Guest"; // Tên của file template, ví dụ home_Guest.html
    }
    @PostMapping("/userLogin")
    public String login(@ModelAttribute("user") Users user, HttpSession session, Model model) {
        Users authenticatedUser = userService.authenticate(user.getEmail(), user.getUserPassword());
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
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

    @GetMapping("/home/{id}")
    public String getConcertForHome(@PathVariable Integer id, Model model) {
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            model.addAttribute("concert", concert);
            return "home";
        }
        return "redirect:/home";
    }

    @ModelAttribute
    public void commonUser(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            Users user = userRepo.findByEmail(email);
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Users user, HttpSession session, Model model, HttpServletRequest request) {
        String url = Utility.getSiteURL(request);

        Users storedUser = userRepo.findByEmail(user.getEmail());
        if (storedUser != null) {
            session.setAttribute("msg", "This email has already been used for registration.");
            return "redirect:/register"; // Redirect back to registration form
        }

        Users savedUser = userService.saveUser(user, url);
        if (savedUser != null) {
            session.setAttribute("msg", "Registration successful. Please check your email for verification.");
        } else {
            session.setAttribute("msg", "Something went wrong on the server.");
        }

        return "redirect:/register";
    }

    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        Users storedUser = userRepo.findByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", storedUser != null);
        return response;
    }

    
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = userService.verifyAccount(code);
        if (verified) {
            model.addAttribute("msg", "Successfully your account is verified");
        } else {
            model.addAttribute("msg", "Maybe your verification code is incorrect or already verified");
        }
        return "message";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/resetPassword?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgotPassword";
    }

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("noodelg@gmail.com", "ConTic");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        Users user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "resetPassword";
    }

    // @PostMapping("/resetPassword")
    // public String processResetPassword(@RequestParam(value = "token") String token,
    //                                    @RequestParam(value = "password") String password,
    //                                    Model model) {
    //     Users user = userService.getByResetPasswordToken(token);
    //     model.addAttribute("title", "Reset your password");

    //     if (user == null) {
    //         model.addAttribute("message", "Invalid Token");
    //         return "message";
    //     } else {
    //         userService.updatePassword(user, password);
    //         model.addAttribute("message", "You have successfully changed your password.");
    //     }

    //     return "login";
    // }
    @PostMapping("/resetPassword")
public String processResetPassword(
        @RequestParam(value = "token") String token,
        @RequestParam(value = "password") String password,
        @RequestParam(value = "confirmPassword") String confirmPassword,
        Model model) {

    model.addAttribute("title", "Reset your password");

    // Check if the token is valid
    Users user = userService.getByResetPasswordToken(token);
    if (user == null) {
        model.addAttribute("message", "Invalid Token");
        return "message";
    }

    // Check if the password and confirmation password match
    if (!password.equals(confirmPassword)) {
        model.addAttribute("error", "Password and confirmation password do not match!");
        return "reset_password_form";
    }

    // Update password and provide success feedback
    userService.updatePassword(user, password);
    model.addAttribute("message", "You have successfully changed your password.");

    return "login";
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
