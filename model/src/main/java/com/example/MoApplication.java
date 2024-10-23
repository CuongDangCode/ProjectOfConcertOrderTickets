// package com.example;

// import java.sql.Date;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import com.example.CRUD.Repository.UserRepository;
// import com.example.mo.Users;

// @SpringBootApplication
// public class MoApplication implements CommandLineRunner {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private BCryptPasswordEncoder passwordEncoder;

//     public static void main(String[] args) {
//         SpringApplication.run(MoApplication.class, args);
//     }

//     @Override
//     public void run(String... args) throws Exception {
//         // Tạo một đối tượng Users cho user
//         Users user = new Users();
//          user.setUserName("Admin");
//         //  user.setEmail("concertowner@gmail.com");
//         user.setEmail("admin@gmail.com");
//         user.setPhone("123456789");
//         user.setUserPassword(passwordEncoder.encode("123123")); // Mã hóa mật khẩu
//         // user.setRole("CINEMA_OWNER");
//         user.setRole("ADMIN");
//         user.setStatus(true);
//         // user.setVerificationCode("some_verification_code");
//         user.setBirthdate(Date.valueOf("1990-01-01")); // Thay đổi ngày sinh nếu cần
//         // user.setLocation("user Location");
//         // user.setGender("Other");
//         // user.setPaymentMethod("Credit Card");
//         // user.setProfileImageURL("default_profile_image_url");
//         // user.setResetPasswordToken("some_reset_token");
//         // user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Hết hạn sau 1 giờ

//         // Lưu thông tin user vào cơ sở dữ liệu
//         userRepository.save(user);

//         System.out.println("Owner user created successfully!");
//     }
//  }
 