package com.example.mo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_room_id", nullable = false)
    private Integer areaID;

    @ManyToOne
    @JoinColumn(name = "yard_id", nullable = false)    
    private Yard yard;

    @Column(length = 45, nullable = false, name = "roomname")
    private String roomname;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes = new ArrayList<>();

    public Area(Integer areaID, String roomName) {
        this.areaID = areaID;
        this.roomname = roomName;
    }

    public Integer getAreaID() {
        return this.areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }

    public Integer getYardID() {
        return this.yard != null ? this.yard.getYardID() : null;
    }

    public void setYard(Yard yard) {
        this.yard = yard;
    }

    public String getRoomname() {
        return this.roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
}
