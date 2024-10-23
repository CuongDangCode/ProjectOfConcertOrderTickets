package com.example.CRUD.service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.CRUD.Repository.FoodRepository;
import com.example.CRUD.Repository.SeatRepository;
import com.example.CRUD.Repository.ShowtimeRepository;
import com.example.CRUD.Repository.TicketRepository;
import com.example.CRUD.Repository.UserRepository;
import com.example.CRUD.Repository.YardRepository;
import com.example.mo.Concert;
import com.example.mo.Food;
import com.example.mo.Seat;
import com.example.mo.Showtime;
import com.example.mo.Ticket;
import com.example.mo.TicketDTO;
import com.example.mo.Users;
import com.example.mo.Yard;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private YardRepository yardRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Map<String, Double> calculateRevenueByConcert() {
        List<Ticket> allTickets = ticketRepository.findAllTicketsWithConcerts();
        Map<String, Double> revenueByConcert = new HashMap<>();
        double totalRevenue = 0.0;

        // Tính tổng doanh thu và doanh thu cho từng phim
        for (Ticket ticket : allTickets) {
            if (ticket.getConcert() != null && ticket.getConcert().getTitle() != null) {
                String concertTitle = ticket.getConcert().getTitle();
                double price = ticket.getPrice();
                revenueByConcert.put(concertTitle, revenueByConcert.getOrDefault(concertTitle, 0.0) + price);
            }
        }

        // Tính tổng doanh thu
        for (double revenue : revenueByConcert.values()) {
            totalRevenue += revenue;
        }

        // Chuyển đổi doanh thu thành phần trăm nếu tổng doanh thu lớn hơn 0
        if (totalRevenue > 0) {
            for (Map.Entry<String, Double> entry : revenueByConcert.entrySet()) {
                revenueByConcert.put(entry.getKey(), (entry.getValue() / totalRevenue) * 100);
            }
        }

        // Log the calculated revenue for debugging purposes
        revenueByConcert.forEach((concert, revenue) -> logger.info("Concert: " + concert + ", Revenue Percentage: " + revenue));

        return revenueByConcert;
    }

    @Transactional(readOnly = true)
    public Map<String, Map<LocalDate, Double>> calculateRevenueByDay() {
        List<Ticket> allTickets = ticketRepository.findAllTicketsWithConcerts();
        Map<String, Map<LocalDate, Double>> revenueByDay = new HashMap<>();

        for (Ticket ticket : allTickets) {
            if (ticket.getShowDate() != null && ticket.getConcert() != null) {
                LocalDate date = ticket.getShowDate().toLocalDate();
                String concertTitle = ticket.getConcert().getTitle();
                double price = ticket.getPrice();

                revenueByDay.computeIfAbsent(concertTitle, k -> new HashMap<>())
                            .merge(date, price, Double::sum);
            }
        }
        return revenueByDay;
    }

    @Transactional(readOnly = true)
    public Map<String, Map<String, Double>> calculateRevenueByMonth() {
        List<Ticket> allTickets = ticketRepository.findAllTicketsWithConcerts();
        Map<String, Map<String, Double>> revenueByMonth = new HashMap<>();

        for (Ticket ticket : allTickets) {
            if (ticket.getShowDate() != null && ticket.getConcert() != null) {
                LocalDate date = ticket.getShowDate().toLocalDate();
                String concertTitle = ticket.getConcert().getTitle();
                String monthYear = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + date.getYear();
                double price = ticket.getPrice();

                revenueByMonth.computeIfAbsent(concertTitle, k -> new HashMap<>())
                              .merge(monthYear, price, Double::sum);
            }
        }
        return revenueByMonth;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllTicketsWithConcerts();
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUserId(int userId) {
        return ticketRepository.findByUserUserId(userId);
    }

    @Transactional
    public void deleteTicketById(int ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Transactional
    public void savePendingTicket(TicketDTO ticketDTO, Users user) {
        try {
            logger.info("Saving ticket: " + ticketDTO);
            int concertId = Integer.parseInt(ticketDTO.getConcertId());
            int yardId = Integer.parseInt(ticketDTO.getYardId());
            int showtimeId = Integer.parseInt(ticketDTO.getShowtimeId());

            Concert concert = concertService.getConcertById(concertId);
            Yard yard = yardRepository.findById(yardId).orElseThrow(() -> new RuntimeException("Yard not found"));
            Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow(() -> new RuntimeException("Showtime not found"));

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setConcert(concert);
            ticket.setYard(yard);
            ticket.setShowtime(showtime);
            ticket.setPrice(ticketDTO.getTotalPrice3());
            ticket.setOrderInfo(ticketDTO.getOrderInfo());
            ticket.setShowDate(ticketDTO.getShowdate());

            Ticket savedTicket = ticketRepository.save(ticket);
            logger.info("Ticket saved with ID: " + savedTicket.getTicketId());

            List<Seat> seats = new ArrayList<>();
            for (Seat seatDTO : ticketDTO.getSelectedSeats()) {
                Seat seat = new Seat();
                seat.setSeatName(seatDTO.getSeatName());
                seat.setSeatType(seatDTO.getSeatType());
                seat.setStatusSeat(true);
                seat.setAreaId(seatDTO.getAreaId());
                seat.setShowtimeId(seatDTO.getShowtimeId());
                seat.setTicket(savedTicket);
                seats.add(seat);
            }
            seatRepository.saveAll(seats);

            if (ticketDTO.getSelectedFood() != null) {
                List<Food> selectedFoods = new ArrayList<>();
                for (String foodDetail : ticketDTO.getSelectedFood()) {
                    String[] details = foodDetail.split(" id");
                    if (details.length > 1) {
                        int foodId = Integer.parseInt(details[1].trim());
                        Food food = foodRepository.findById(foodId)
                                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
                        selectedFoods.add(food);
                    }
                }
                ticket.setFoods(selectedFoods);
            }

            ticketRepository.save(ticket);
            logger.info("Ticket fully saved with seats and foods.");
        } catch (Exception e) {
            logger.error("Error saving ticket: " + e.getMessage(), e);
            throw new RuntimeException("Error saving ticket: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(int ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));
    }
}
