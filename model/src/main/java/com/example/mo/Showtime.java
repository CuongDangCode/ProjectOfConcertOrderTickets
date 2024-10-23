package com.example.mo;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private Integer showtimeID;

    @Column(nullable = false)
    private Integer concertOwnerID;

    @Column(name = "concert_id", nullable = false)
    private Integer concertID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", insertable = false, updatable = false)
    private Concert concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yard_id", nullable = false)
    private Yard yard;

    @Column(name = "show_date", nullable = false)
    private Date showDate;

    @Column(name = "show_time", nullable = false)
    private Time showTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "showtimeID=" + showtimeID +
                ", concertOwnerID=" + concertOwnerID +
                ", concertID=" + concertID +
                ", showDate=" + showDate +
                ", showTime=" + showTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Getters and setters
    public Integer getShowtimeID() {
        return showtimeID;
    }

    public void setShowtimeID(Integer showtimeID) {
        this.showtimeID = showtimeID;
    }

    public Integer getConcertOwnerID() {
        return concertOwnerID;
    }

    public void setConcertOwnerID(Integer concertOwnerID) {
        this.concertOwnerID = concertOwnerID;
    }

    public Integer getConcertID() {
        return concertID;
    }

    public void setConcertID(Integer concertID) {
        this.concertID = concertID;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Yard getYard() {
        return yard;
    }

    public void setYard(Yard yard) {
        this.yard = yard;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public Time getShowTime() {
        return showTime;
    }

    public void setShowTime(Time showTime) {
        this.showTime = showTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
