package com.example.crud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.crud.repository.UserRepository;
import com.example.mo.Users;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpSession;

@Service("userServiceImpl")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Cập nhật thông tin người dùng
    public Users updateUser(Users users) {
        Users existingUser = userRepository.findById(users.getUserId()).orElse(null);
        if (existingUser != null) {
            // Cập nhật các trường thông tin của người dùng
            existingUser.setUserName(users.getUserName());
            existingUser.setEmail(users.getEmail());
            existingUser.setBirthdate(users.getBirthdate());
            existingUser.setProfileImageURL(users.getProfileImageURL());
            existingUser.setPhone(users.getPhone());
            existingUser.setLocation(users.getLocation());
            existingUser.setGender(users.getGender());
            return userRepository.save(existingUser);
        }
        return null; // Trả về null nếu người dùng không tồn tại
    }

    // Xác thực người dùng với email và mật khẩu
    public Users authenticate(String email, String password) {
        Users user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getUserPassword())) {
            return user; // Trả về người dùng nếu xác thực thành công
        }
        return null; // Trả về null nếu xác thực thất bại
    }

    // Cập nhật mật khẩu của người dùng
    public boolean updatePassword(int userId, String currentPassword, String newPassword) {
        Users existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            if (passwordEncoder.matches(currentPassword, existingUser.getUserPassword())) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                existingUser.setUserPassword(encodedNewPassword);
                userRepository.save(existingUser);
                return true; // Mật khẩu được cập nhật thành công
            }
        }
        return false; // Cập nhật mật khẩu thất bại
    }

    // Lấy danh sách tất cả người dùng
    public List<Users> getUsers() {
        return userRepository.findAll(); // Trả về danh sách người dùng
    }

    // Lấy thông tin người dùng theo ID
    public Users getUsersById(int id) {
        return userRepository.findById(id).orElse(null); // Trả về người dùng nếu tồn tại
    }

    // Lấy thông tin người dùng theo email
    public Users getUsersByEmail(String email) {
        return userRepository.findByEmail(email); // Trả về người dùng nếu tồn tại
    }

    // Lấy thông tin người dùng theo tên người dùng
    public Users getUserByUserName(String username) {
        Users user = userRepository.findByUserName(username);
        return user; // Trả về người dùng nếu tồn tại
    }

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    // Lưu thông tin người dùng mới và gửi email xác minh
    public Users saveUser(Users user, String url) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new EntityExistsException("Email already exists"); // Ném ngoại lệ nếu email đã tồn tại
        }

        // Mã hóa mật khẩu của người dùng
        String password = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(password);
        
        // Thiết lập vai trò và mã xác minh cho người dùng
        user.setRole("ROLE_USER");
        user.setStatus(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        // Lưu người dùng vào cơ sở dữ liệu
        Users newUser = userRepo.save(user);

        // Gửi email xác minh nếu người dùng được lưu thành công
        if (newUser != null) {
            sendEmail(newUser, url);
        }

        return newUser; // Trả về người dùng mới
    }

    // Gửi email xác minh tài khoản cho người dùng
    public void sendEmail(Users user, String url) {
        String from = "noodelg@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "ConTic";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            // Thiết lập thông tin gửi email
            helper.setFrom(from, "ConTic");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getUserName());
            String siteUrl = url + "/verify?code=" + user.getVerificationCode();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message); // Gửi email

        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }

    // Xác minh tài khoản người dùng bằng mã xác minh
    public boolean verifyAccount(String verificationCode) {
        Users user = userRepo.findByVerificationCode(verificationCode);

        if (user == null) {
            return false; // Trả về false nếu không tìm thấy người dùng
        } else {
            // Thiết lập trạng thái tài khoản là đã xác minh
            user.setStatus(true);
            user.setVerificationCode(null);

            userRepo.save(user); // Lưu người dùng
            return true; // Trả về true nếu xác minh thành công
        }
    }

    // Xóa thông báo trong phiên làm việc
    public void removeSessionMessage() {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();
        session.removeAttribute("msg"); // Xóa thuộc tính "msg" khỏi session
    }

    // Cập nhật token đặt lại mật khẩu cho người dùng
    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(5)); // Token sẽ hết hạn sau 5 phút
            userRepository.save(user); // Lưu người dùng
        } else {
            throw new UsernameNotFoundException("Could not find any user with the email " + email); // Ném ngoại lệ nếu không tìm thấy người dùng
        }
    }

    // Lấy thông tin người dùng theo token đặt lại mật khẩu
    public Users getByResetPasswordToken(String token) {
        Users user = userRepository.findByResetPasswordToken(token); // Sử dụng userRepository đã tiêm vào
        
        // Kiểm tra nếu token đã hết hạn
        if (user != null && user.getResetPasswordTokenExpiry() != null && 
            user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            return null; // Trả về null nếu token đã hết hạn
        }
        
        return user; // Trả về user nếu token hợp lệ
    }

    // Cập nhật mật khẩu mới cho người dùng
    public void updatePassword(Users user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedPassword);

        user.setResetPasswordToken(null); // Xóa token đặt lại mật khẩu
        userRepository.save(user); // Lưu người dùng
    }
}
