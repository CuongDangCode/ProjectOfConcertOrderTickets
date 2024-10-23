package com.example.mo;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Yard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yard_id")
    private Integer yardID;


    @Column(nullable = false)
    private Integer concertOwnerID;


    @Column(name = "concert_id", nullable = false)
    private String concertID; // Changed to String


    @Column(length = 45, nullable = false)
    private String yardName;


    @Column(length = 1000, nullable = false)
    private String address;


    @Column(length = 2000, nullable = false)
    private String description;


    @Column(nullable = true, length = 64, name = "photoYard")
    private String photoYard;


    @Column(nullable = false)
    private Double rating;


    @OneToMany(mappedBy = "yard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes;


    @Transient // Add this annotation to exclude from persistence
    private MultipartFile photoYardFile;


    @ManyToMany
@JoinTable(name = "yard_concert",
           joinColumns = @JoinColumn(name = "yard_id"),
           inverseJoinColumns = @JoinColumn(name = "concert_id"))
private Set<Concert> concerts = new HashSet<>();

    // Getter and setter for yardID
    public Integer getYardID() {
        return yardID;
    }


    public void setYardID(Integer yardID) {
        this.yardID = yardID;
    }


    // Getter and setter for concertOwnerID
    public Integer getConcertOwnerID() {
        return concertOwnerID;
    }


    public void setConcertOwnerID(Integer concertOwnerID) {
        this.concertOwnerID = concertOwnerID;
    }


    // Getter and setter for concertID
    public String getConcertID() {
        return concertID;
    }


    public void setConcertID(String concertID) {
        this.concertID = concertID;
    }


    // Getter and setter for yardName
    public String getYardName() {
        return yardName;
    }


    public void setYardName(String yardName) {
        this.yardName = yardName;
    }


    // Getter and setter for address
    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    // Getter and setter for description
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    // Transient method to construct photo path
    @Transient
    public String getPhotosImagePath() {
        if (photoYard == null)
            return null;
        return "/yard-photo/" + yardID + "/" + photoYard;
    }


    // Getter and setter for photoYard
    public String getPhotoYard() {
        return photoYard;
    }


    public void setPhotoYard(String photoYard) {
        this.photoYard = photoYard;
    }


    // Getter and setter for rating
    public Double getRating() {
        return rating;
    }


    public void setRating(Double rating) {
        this.rating = rating;
    }


    // Getter and setter for showtimes
    public List<Showtime> getShowtimes() {
        return showtimes;
    }


    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
}


