package com.example.CRUD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mo.Food;
import com.example.mo.FoodDTO;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByConcertOwnerID(Integer concertOwnerID);

     @Query("SELECT new com.example.mo.FoodDTO(f.foodID, f.concertOwnerID, f.foodName, f.price, f.photoFood) " +
           "FROM Food f WHERE f.concertOwnerID = :concertOwnerId")
    List<FoodDTO> getFoodByConcertOwnerId(@Param("concertOwnerId") int concertOwnerId);
}
