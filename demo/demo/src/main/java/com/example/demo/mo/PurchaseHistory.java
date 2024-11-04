package com.example.mo;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseID; // Use camelCase for field names

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false) // Adding nullable = false for better integrity
    private Users user;

    private int ticketId; // Use camelCase for field names

    private Date purchaseDate; // Use camelCase for field names

    private BigDecimal totalAmount; // Use camelCase for field names
}
