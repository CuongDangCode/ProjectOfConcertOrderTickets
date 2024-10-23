package com.example.mo;


// import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concertID;


    @Column(nullable = false)
    private Integer concertOwnerID;


    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "genre", nullable = false)
    private String genre;


    // @Column(name = "duration", nullable = false)
    // private Integer duration;


    // @Column(name = "director", nullable = false)
    // private String director;


    @Column(name = "cast", nullable = false)
    private String cast;


    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    // @Column(name = "releaseDate", nullable = false)
    // private Date releaseDate;


    // @Column(name = "languages", nullable = false)
    // private String languages;


    @Column(name = "ratingCount")
    private Integer ratingCount = 0;


    @Column(name = "averageRating")
    private Double averageRating = 0.0;


    @Column(name = "description", nullable = false, length = 1000)
    private String description;


    // @Column(name = "trailerURL", nullable = false)
    // private String trailerURL;


    @Column(name = "address", nullable = false)
    private String address;


    @Column(name = "status_concert", nullable = false)
    private String statusConcert;


    @OneToMany(mappedBy = "concert")
    private List<Showtime> showtimes;


    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;


    @ElementCollection
    @CollectionTable(name = "concert_owner_votes", joinColumns = @JoinColumn(name = "concert_id"))
    @Column(name = "user_id")
    private List<Integer> concertOwnerVotes;


    @ManyToMany(mappedBy = "concerts")
    private Set<Yard> yards = new HashSet<>();


    public List<Integer> getConcertOwnerVotes() {
        return concertOwnerVotes;
    }


    public void setConcertOwnerVotes(List<Integer> concertOwnerVotes) {
        this.concertOwnerVotes = concertOwnerVotes;
    }


    public void addVote(Integer userId) {
        this.concertOwnerVotes.add(userId);
    }


    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public enum StatusConcert {
        NOW_SHOWING("NOW_SHOWING"),
        COMING_SOON("COMING_SOON");


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
        // this.duration = concertDetails.getDuration();
        // this.director = concertDetails.getDirector();
        this.cast = concertDetails.getCast();
        // this.releaseDate = concertDetails.getReleaseDate();
        // this.languages = concertDetails.getLanguages();
        this.ratingCount = concertDetails.getRatingCount();
        this.averageRating = concertDetails.getAverageRating();
        this.description = concertDetails.getDescription();
        // this.trailerURL = concertDetails.getTrailerURL();
        this.address = concertDetails.getAddress();
        this.statusConcert = concertDetails.getStatusConcert();
    }


    @Override
    public String toString() {
        return "Concert{" +
                "concertID=" + concertID +
                ", concertOwnerID=" + concertOwnerID +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                // ", duration=" + duration +
                // ", director='" + director + '\'' +
                ", cast='" + cast + '\'' +
                // ", releaseDate=" + releaseDate +
                // ", languages='" + languages + '\'' +
                ", ratingCount=" + ratingCount +
                ", averageRating=" + averageRating +
                ", description='" + description + '\'' +
                // ", trailerURL='" + trailerURL + '\'' +
                ", address='" + address + '\'' +
                ", statusConcert='" + statusConcert + '\'' +
                '}';
    }
}




