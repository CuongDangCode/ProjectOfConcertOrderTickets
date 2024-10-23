package com.example.mo;

import java.util.Date;

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
    private int concertOwnerID;

    @OneToOne
    @JoinColumn(name = "UserId")
    private Users users;

    private String Hotline;
    private String Email;

    // Wallet attributes
    private Double walletBalance = 0.0;
    private Date walletLastUpdated = new Date();

    // Methods to manage the wallet
    public void addFundsToWallet(Double amount) {
        if (amount > 0) {
            this.walletBalance += amount;
            this.walletLastUpdated = new Date();
        } else {
            throw new IllegalArgumentException("Amount to add must be greater than zero.");
        }
    }

    public void withdrawFundsFromWallet(Double amount) {
        if (amount > 0 && this.walletBalance >= amount) {
            this.walletBalance -= amount;
            this.walletLastUpdated = new Date();
        } else {
            throw new IllegalArgumentException("Insufficient funds or invalid amount.");
        }
    }

    @Override
    public String toString() {
        return "ConcertOwner{" +
                "concertOwnerID=" + concertOwnerID +
                ", Hotline='" + Hotline + '\'' +
                ", Email='" + Email + '\'' +
                ", walletBalance=" + walletBalance +
                ", walletLastUpdated=" + walletLastUpdated +
                '}';
    }
}
