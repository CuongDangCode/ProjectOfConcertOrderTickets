package com.example.crud.repository;

// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;

// import com.example.mo.Users;

// public interface UserLoginRepository extends JpaRepository<Users, Integer> {
//     Optional<Users> findByEmail(String email);

//     @Modifying
//     @Query("UPDATE User u SET u.username = :userName, u.email = :email WHERE u.userId = :userId")
//     void update(String userName, String email, int userId);
// }

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserLoginRepository {

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.username = :userName, u.email = :email WHERE u.user_id = :userId")
    void update(@Param("userName") String userName, @Param("email") String email, @Param("userId") int userId);
}