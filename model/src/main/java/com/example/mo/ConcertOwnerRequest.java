package com.example.mo;

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
public class ConcertOwnerRequest {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int concertOwnerRequestId;
@ManyToOne
@JoinColumn(name = "userId")
private Users users;
private String hotline;
private String email;
}