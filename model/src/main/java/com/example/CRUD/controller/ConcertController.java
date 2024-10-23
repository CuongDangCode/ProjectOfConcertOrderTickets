package com.example.CRUD.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.CRUD.service.ConcertService;
import com.example.CRUD.service.RatingService;
import com.example.CRUD.service.UserService;
import com.example.CRUD.service.YardService;
import com.example.mo.Concert;
import com.example.mo.Rating;
import com.example.mo.Users;
import com.example.mo.Yard;

@Controller
@RequestMapping("/concert")
public class ConcertController {

    private final ConcertService concertService;
    private final RatingService ratingService;
    private final UserService userService;
    @Autowired
    private YardService yardService;

    public ConcertController(ConcertService concertService, UserService userService, RatingService ratingService) {
        this.concertService = concertService;
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllConcerts(Model model, Principal principal) {
        List<Concert> concerts = concertService.getAllConcerts();
        model.addAttribute("concerts", concerts);
        return "concert";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("concert", new Concert());
        return "concert-form";
    }

    @PostMapping("/create")
    public String createConcert(@ModelAttribute Concert concert, @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Principal principal) {
        concert.setRatingCount(0);
        concert.setAverageRating(0.0);
        concert.setConcertOwnerID(getConcertOwnerIDFromPrincipal(principal));
        if (!imageFile.isEmpty()) {
            try {
                // Lưu tệp tải lên vào thư mục cục bộ
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                String fileName = imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());
                concert.setAddress("/uploads/" + fileName); // Lưu đường dẫn ảnh vào thuộc tính address
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Không thể tải lên tệp ảnh.");
                return "redirect:/concert/new";
            }
        }
        concertService.saveConcert(concert);
        return "redirect:/concert";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, Principal principal) {
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            Integer concertOwnerID = getConcertOwnerIDFromPrincipal(principal);
            List<Yard> listYard = yardService.listAllByConcertOwnerID(concertOwnerID);
            model.addAttribute("concert", concert);
            model.addAttribute("listYard", listYard);
            return "concert-form2";
        }
        return "redirect:/concert";
    }

    @PostMapping("/update/{id}")
public String updateConcert(@PathVariable Integer id, @ModelAttribute Concert concertDetails,
                          @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes) {
                            Concert concert = concertService.getConcertById(id);
    if (concert != null) {
        if (!concert.getTitle().equalsIgnoreCase(concertDetails.getTitle()) && concertService.isDuplicateTitle(concertDetails.getTitle())) {
            redirectAttributes.addFlashAttribute("message", "Đã có bộ phim với title này.");
            return "redirect:/concert/edit/" + id;
        }

        if (imageFile.isEmpty() && (concert.getAddress() == null || concert.getAddress().isEmpty())) {
            redirectAttributes.addFlashAttribute("message", "Bạn hãy upload ảnh phim.");
            return "redirect:/concert/edit/" + id;
        }

        try {
            if (!imageFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String fileName = imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, imageFile.getBytes());

                concert.setAddress("/uploads/" + fileName);
            }

            concert.updateDetails(concertDetails);
            concertService.saveConcert(concert);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Không thể tải lên tệp ảnh.");
            return "redirect:/concert/edit/" + id;
        }
    } else {
        redirectAttributes.addFlashAttribute("message", "Bộ phim không tồn tại.");
    }
    return "redirect:/concert";
}
    @GetMapping("/home/{id}")
    public String getConcertForHome(@PathVariable Integer id, Model model) {
        Concert concert = concertService.getConcertById(id);
        if (concert != null) {
            model.addAttribute("concert", concert);
            return "home";
        }
        return "redirect:/concert";
    }

    @GetMapping("/home")
    public String getAllConcertsForHome(Model model, Principal principal) {
        Users user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);

        List<Concert> concerts = concertService.getAllConcerts();
        List<Concert> simplifiedConcerts = concerts.stream()
                .map(m -> {
                    Concert simplifiedConcert = new Concert();
                    simplifiedConcert.setTitle(m.getTitle());
                    simplifiedConcert.setAddress(m.getAddress());
                    return simplifiedConcert;
                })
                .collect(Collectors.toList());
        model.addAttribute("concerts", simplifiedConcerts);
        return "home";
    }

    @PostMapping("/vote/{id}")
    public String voteForConcert(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Users user = userService.getUsersByEmail(principal.getName());
            concertService.voteForConcert(id, user.getUserId());
            redirectAttributes.addFlashAttribute("message", "Vote successful!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/concert";
    }

    @GetMapping("/book/{concertId}")
    public String getConcertRatings(@PathVariable Integer concertId,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  Model model, Principal principal) {
                                    Concert concert = concertService.getConcertById(concertId);
        if (concert == null) {
            return "redirect:/error";
        }

        List<Rating> ratings = ratingService.getAllRatingsByConcertId(concertId);

        int start = Math.min(page * size, ratings.size());
        int end = Math.min((page + 1) * size, ratings.size());

        List<Rating> paginatedRatings = ratings.subList(start, end);

        model.addAttribute("concert", concert);
        model.addAttribute("ratings", paginatedRatings);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) ratings.size() / size));
        String email = principal.getName();
        Users user = userService.getUsersByEmail(email);

        model.addAttribute("user", user);
        return "book";
    }

    @GetMapping("/search")
    public String searchConcerts(@RequestParam(name = "keyword", required = false) String keyword, Model model, Principal p) {
        List<Concert> allConcerts = concertService.getAllConcerts();
        List<Concert> comingSoonConcerts = concertService.getAllComingSoonConcerts();
        List<Concert> concerts = allConcerts;
        String email = p.getName();
        Users user = userService.getUsersByEmail(email);

        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("message", "Bạn hãy nhập tên phim");
        } else {
            concerts = concerts.stream().filter(m -> m.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());

            if (concerts.isEmpty()) {
                model.addAttribute("message", "Không tìm thấy phim nào");
                concerts = allConcerts; // Hiển thị tất cả phim nếu không tìm thấy phim nào
            }
        }

        List<Concert> simplifiedConcerts = concerts.stream()
                .map(m -> {
                    Concert simplifiedConcert = new Concert();
                    simplifiedConcert.setConcertID(m.getConcertID());
                    simplifiedConcert.setTitle(m.getTitle());
                    simplifiedConcert.setAddress(m.getAddress());
                    return simplifiedConcert;
                })
                .collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("comingSoonConcerts", comingSoonConcerts);
        model.addAttribute("concerts", simplifiedConcerts);
        return "home"; // Trả về view 'home' để hiển thị kết quả tìm kiếm
    }

    @GetMapping("/general/genre")
    public String searchByGenre(@RequestParam("genre") String genre, Model model, Principal p) {
        String email = p.getName();
        Users user = userService.getUsersByEmail(email);
        List<Concert> concertsByGenre = concertService.getConcertsByGenre(genre);
        List<Concert> comingSoonConcerts = concertService.getAllComingSoonConcerts();
        model.addAttribute("user", user);
        model.addAttribute("comingSoonConcerts", comingSoonConcerts);
        model.addAttribute("concerts", concertsByGenre);
        return "home";
    }

    private Integer getConcertOwnerIDFromPrincipal(Principal principal) {
        Users user = userService.getUsersByEmail(principal.getName());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getUserId(); // Ensure this returns the correct ID for concert owner
    }
}
