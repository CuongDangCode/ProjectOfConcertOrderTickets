package com.example.mo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcertOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cinemaOwnerID;

    @OneToOne
    @JoinColumn(name = "userID")
    private Users users;

    private String concertName;
    private String addressConcert;
    private String hotline;
    private String email;
    // private int employeeID;
}