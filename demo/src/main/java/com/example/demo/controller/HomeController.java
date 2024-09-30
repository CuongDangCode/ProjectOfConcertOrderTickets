package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.crud.repository.UserRepository;
import com.example.crud.service.UserService;
import com.example.mo.Users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.bytebuddy.utility.RandomString;

@Controller
public class HomeController {

    // Inject UserService và UserRepository
    private final UserService userService;
    private final UserRepository userRepo;

    // Constructor để inject dependencies
    public HomeController(UserService userService, UserRepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    // Xử lý yêu cầu đăng nhập của người dùng
    @PostMapping("/userLogin")
    public String login(@ModelAttribute("user") Users user, HttpSession session, Model model) {
        // Xác thực thông tin đăng nhập của người dùng
        Users authenticatedUser = userService.authenticate(user.getEmail(), user.getUserPassword());
        if (authenticatedUser != null) {
            // Lưu thông tin người dùng vào session và chuyển hướng tới trang home
            session.setAttribute("user", authenticatedUser);
            return "redirect:/home";
        } else {
            // Hiển thị thông báo lỗi nếu email hoặc mật khẩu không hợp lệ
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // Thêm thông tin người dùng vào model để sử dụng chung cho các view
    @ModelAttribute
    public void commonUser(Principal p, Model m) {
        if (p != null) {
            // Lấy thông tin email của người dùng hiện tại từ Principal
            String email = p.getName();
            Users user = userRepo.findByEmail(email);
            // Thêm thông tin người dùng vào model
            m.addAttribute("user", user);
        }
    }

    // Hiển thị trang đăng ký người dùng
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Hiển thị trang đăng nhập người dùng
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Lưu thông tin người dùng mới đăng ký vào cơ sở dữ liệu
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Users user, HttpSession session, Model m, HttpServletRequest request) {
        try {
            // Tạo URL xác nhận tài khoản
            String url = request.getRequestURL().toString();
            url = url.replace(request.getServletPath(), "");
            // Lưu người dùng và gửi email xác nhận
            Users u = userService.saveUser(user, url);
            session.setAttribute("msg", "Register successfully");
        } catch (EntityExistsException e) {
            // Xử lý ngoại lệ khi email đã tồn tại
            session.setAttribute("msg", "Email already exists");
        } catch (Exception e) {
            // Xử lý ngoại lệ khác
            session.setAttribute("msg", "Something went wrong on the server");
        }
        return "redirect:/register";
    }

    // Xác minh tài khoản người dùng bằng mã xác thực
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model m) {
        boolean f = userService.verifyAccount(code);

        if (f) {
            m.addAttribute("msg", "Successfully your account is verified");
        } else {
            m.addAttribute("msg", "Maybe your verification code is incorrect or already verified");
        }

        return "message";
    }

    // Hiển thị trang quên mật khẩu
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    // Xử lý yêu cầu đặt lại mật khẩu
    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            // Cập nhật token đặt lại mật khẩu và gửi email cho người dùng
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

    // Gửi email với đường link đặt lại mật khẩu
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // Thiết lập thông tin gửi email
        helper.setFrom("noodelg@gmail.com", "Oh no! You forgot your password.");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        // Nội dung email
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    // Hiển thị form đặt lại mật khẩu với token
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

    // Xử lý yêu cầu đặt lại mật khẩu mới
    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam(value = "token") String token,
                                       @RequestParam(value = "password") String password,
                                       Model model) {
        Users user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message"; 
        } else {
            // Cập nhật mật khẩu mới cho người dùng
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "login"; // Chuyển hướng tới trang login sau khi đặt lại mật khẩu thành công
    }

    // Lớp tiện ích để lấy URL của trang web hiện tại
    public class Utility {
        public static String getSiteURL(HttpServletRequest request) {
            String siteURL = request.getRequestURL().toString();
            return siteURL.replace(request.getServletPath(), "");
        }
    }
}

