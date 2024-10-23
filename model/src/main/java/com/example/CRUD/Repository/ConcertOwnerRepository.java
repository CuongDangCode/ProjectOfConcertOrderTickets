// package com.example.CRUD.Repository;


// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.example.mo.CinemaOwner;


// @Repository
// public interface CinemaOwnerRepository extends JpaRepository<CinemaOwner, Integer> {
//     List<CinemaOwner> findByCinemaNameContainingIgnoreCase(String cinemaName);
//     CinemaOwner findByUsersUserId(int userId);
// }

package com.example.CRUD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mo.ConcertOwner;

@Repository
public interface ConcertOwnerRepository extends JpaRepository<ConcertOwner, Integer> {
    // Xóa phương thức tìm kiếm theo cinemaName
    // Nếu bạn cần thêm các phương thức khác, hãy thêm ở đây

    ConcertOwner findByUsersUserId(int userId);
}