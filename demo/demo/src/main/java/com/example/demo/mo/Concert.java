package com.example.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concertID;

    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "director")
    private String director;

    @Column(name = "cast")
    private String cast;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "releaseDate")
    private Date releaseDate;

    @Column(name = "showTime")
    private String showTime;

    @Column(name = "languages")
    private String languages;

    @Column(name = "ratingCount")
    private Integer ratingCount = 0;

    @Column(name = "averageRating")
    private Double averageRating = 0.0;
    @Column(name = "description")
    private String description;

    @Column(name = "trailerURL")
    private String trailerURL;

    @Column(name = "address")
    private String address;

    @Column(name = "status_concert")
    private String statusConcert;
    @ManyToOne
    @JoinColumn(name = "concertOwnerID")
    private ConcertOwner concertOwner;

    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public enum StatusConcert {
        NOW_GOING("Đang Diễn ra"),
        COMING_SOON("Sắp Diễn ra");

        private final String status;

        StatusConcert(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public void updateDetails(Concert concertDetails) {
        this.title = concertDetails.getTitle();
        this.genre = concertDetails.getGenre();
        this.duration = concertDetails.getDuration();
        this.director = concertDetails.getDirector();
        this.cast = concertDetails.getCast();
        this.releaseDate = concertDetails.getReleaseDate();
        this.showTime = concertDetails.getShowTime();
        this.languages = concertDetails.getLanguages();
        this.ratingCount = concertDetails.getRatingCount();
        this.averageRating = concertDetails.getAverageRating();
        this.description = concertDetails.getDescription();
        this.trailerURL = concertDetails.getTrailerURL();
        this.address = concertDetails.getAddress(); // Cập nhật thuộc tính này
    }
}