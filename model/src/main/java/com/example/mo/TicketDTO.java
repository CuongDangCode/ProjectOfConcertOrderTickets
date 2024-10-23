package com.example.mo;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TicketDTO {
    private String userId;
    private String concertId;
    private String showtimeId;
    private String yardId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date showdate;
    private List<Seat> selectedSeats;
    private List<String> selectedFood;
    private double totalPrice3;
    private String orderInfo; // Added orderInfo field

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public String getYardId() {
        return yardId;
    }

    public void setYardId(String yardId) {
        this.yardId = yardId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Date getShowdate() {
        return showdate;
    }

    public void setShowdate(Date showdate) {
        this.showdate = showdate;
    }

    public List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<Seat> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public List<String> getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFood(List<String> selectedFood) {
        this.selectedFood = selectedFood;
    }

    public double getTotalPrice3() {
        return totalPrice3;
    }

    public void setTotalPrice3(double totalPrice3) {
        this.totalPrice3 = totalPrice3;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    // toString method
    @Override
    public String toString() {
        return "TicketDTO{" +
                "userId='" + userId + '\'' +
                ", concertId='" + concertId + '\'' +
                ", showtimeId='" + showtimeId + '\'' +
                ", yardId='" + yardId + '\'' +
                ", showdate=" + showdate +
                ", selectedSeats=" + selectedSeats +
                ", selectedFood=" + selectedFood +
                ", totalPrice3=" + totalPrice3 +
                ", orderInfo='" + orderInfo + '\'' +
                '}';
    }
}
