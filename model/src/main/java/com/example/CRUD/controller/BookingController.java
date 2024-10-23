package com.example.CRUD.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.CRUD.Repository.AreaRepository;
import com.example.CRUD.Repository.FoodRepository;
import com.example.CRUD.Repository.SeatRepository;
import com.example.CRUD.Repository.ShowtimeRepository;
import com.example.CRUD.Repository.TicketRepository;
import com.example.CRUD.Repository.UserRepository;
import com.example.CRUD.Repository.YardRepository;
import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.FoodService;
import com.example.CRUD.service.SeatService;
import com.example.CRUD.service.ShowtimeService;
import com.example.CRUD.service.TicketService;
import com.example.CRUD.service.UserService;
import com.example.CRUD.service.YardService;
import com.example.mo.Area;
import com.example.mo.Concert;
import com.example.mo.Food;
import com.example.mo.FoodDTO;
import com.example.mo.Seat;
import com.example.mo.SeatDTO;
import com.example.mo.Showtime;
import com.example.mo.ShowtimeDTO;
import com.example.mo.Ticket;
import com.example.mo.TicketDTO;
import com.example.mo.Users;
import com.example.mo.Yard;

import jakarta.mail.internet.ParseException;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {
    @Autowired
    private UserService userService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private YardService yardSer;
    @Autowired
    private ShowtimeService showtimeSer;
    @Autowired
    private SeatService seatService;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private YardRepository yardRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FoodService foodSer;

    @GetMapping("/booking/{userid}/{concertid}")
    public String showBookingPage(Model model, @PathVariable("concertid") Integer concertid,
            @PathVariable("userid") Integer userid) {
                Concert concert = concertService.getConcertById(concertid);
        List<Yard> yards = yardSer.listAll();
        List<Food> foods = foodSer.listAll();
        Users user = userService.getUserById(userid);
        // String userRank = user.getUserRank();

        Map<String, List<Yard>> yardMap = yards.stream()
                .collect(Collectors.groupingBy(Yard::getYardName));

        model.addAttribute("user", user);
        model.addAttribute("yardMap", yardMap);
        model.addAttribute("concert", concert);
        model.addAttribute("foods", foods);
        model.addAttribute("concertID", concert.getConcertID());
        // model.addAttribute("userRank", userRank);
        return "booking";
    }

    @GetMapping("/showtimes/getShowtimes")
    @ResponseBody
    public List<ShowtimeDTO> getShowtimesByConcertIDAndYardID(@RequestParam("concertID") Integer concertID,
            @RequestParam("yardID") Integer yardID) {
        List<Object[]> results = showtimeSer.getShowtimesByConcertIDAndYardID(concertID, yardID);
        List<ShowtimeDTO> showtimes = new ArrayList<>();
        for (Object[] result : results) {
            Date showDate = (Date) result[0];
            Time showTime = (Time) result[1];
            Integer showtimeId = (Integer) result[2];
            showtimes.add(new ShowtimeDTO(showtimeId, showDate, showTime));
        }
        return showtimes;
    }

    @GetMapping("/showtimes/getRoomName")
    @ResponseBody
    public List<Area> getRoomName(@RequestParam int yardID,
            @RequestParam int concertID,
            @RequestParam Time showTime,
            @RequestParam Date showDate,
            @RequestParam int showTimeId) {
        List<Area> area = areaRepository
                .findAreasByYardIdAndConcertIdAndShowTimeAndShowDate(yardID, concertID, showTime, showDate,
                        showTimeId);
        return area;
    }

    @PostMapping("/tickets/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveTicket(@RequestBody TicketDTO ticketDTO, HttpSession session)
            throws ParseException {
        try {
            int userId = parseId(ticketDTO.getUserId(), "userId");
            Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            session.setAttribute("ticketDTO", ticketDTO);
            int amount = (int) ticketDTO.getTotalPrice3();
            String redirectUrl = "/createOrder?amount=" + amount;

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Ticket information stored in session. Proceed to payment.");
            response.put("redirectUrl", redirectUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/tickets/saveAfterPayment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveTicketAfterPayment(HttpSession session) throws ParseException {
        try {
            TicketDTO ticketDTO = (TicketDTO) session.getAttribute("ticketDTO");
            if (ticketDTO == null) {
                throw new RuntimeException("No ticket information found in session.");
            }

            int userId = parseId(ticketDTO.getUserId(), "userId");
            int concertId = parseId(ticketDTO.getConcertId(), "concertId");
            int yardId = Integer.parseInt(ticketDTO.getYardId());
            int showtimeId = Integer.parseInt(ticketDTO.getShowtimeId());
            Date showdate = (ticketDTO.getShowdate());

            Concert concert = concertService.getConcertById(concertId);
            Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Yard yard = yardRepository.findById(yardId)
                    .orElseThrow(() -> new RuntimeException("Yard not found"));
            Showtime showtime = showtimeRepository.findById(showtimeId)
                    .orElseThrow(() -> new RuntimeException("Showtime not found"));

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setConcert(concert);
            ticket.setYard(yard);
            ticket.setShowtime(showtime);
            ticket.setShowDate(showdate);
            ticket.setPrice(ticketDTO.getTotalPrice3());

            Ticket savedTicket = ticketRepository.save(ticket);

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
            ticketRepository.save(ticket);

            session.removeAttribute("ticketDTO");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Ticket saved successfully after payment.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private int parseId(String id, String fieldName) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không thể null hoặc trống");
        }
        return Integer.parseInt(id);
    }

    @GetMapping("/seats/booked")
    @ResponseBody
    public List<SeatDTO> getBookedSeats(@RequestParam Integer areaId,
            @RequestParam Integer showtimeId) {
        List<Object[]> rusults = seatService.getBookedSeats(areaId, showtimeId);
        List<SeatDTO> bookedSeats = new ArrayList<>();
        for (Object[] result : rusults) {
            String seatName = (String) result[0];
            String seatType = (String) result[1];
            boolean statusSeat = (boolean) result[2];
            bookedSeats.add(new SeatDTO(seatName, seatType, statusSeat));
        }
        return bookedSeats;
    }

    @GetMapping("/getFoodByYardId/{yardID}")
    @ResponseBody
    public List<FoodDTO> getFoodByConcertOwnerId(@PathVariable int yardID) {
        Integer concertOwnerId = yardSer.findConcertOwnerIdByYardId(yardID);
        List<FoodDTO> foods = foodSer.getFoodByConcertOwnerId(concertOwnerId);
        return foods;
    }

    @DeleteMapping("tickets/delete/{ticketId}")
    public String deleteTicket(@PathVariable int ticketId) {
        ticketService.deleteTicketById(ticketId);
        return "redirect:/mytickets";
    }
}
