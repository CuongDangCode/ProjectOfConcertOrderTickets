package com.example.mo;

import java.sql.Date;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int userId;

    private String userName;
    private String email;
    private String phone;
    private String userPassword;
    private String role;
    private boolean status;
    private String verificationCode;
    private Date birthdate;
    private String location;
    private String gender;
    private String paymentMethod;
    private String profileImageURL;
    private String resetPasswordToken;
    @OneToOne(mappedBy = "users")
    private ConcertOwner concertOwner;
    private LocalDateTime resetPasswordTokenExpiry;

public LocalDateTime getResetPasswordTokenExpiry() {
    return resetPasswordTokenExpiry;
}

public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) {
    this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
}
}