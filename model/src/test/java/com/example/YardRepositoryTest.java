package com.example;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.CRUD.Repository.YardRepository;
import com.example.mo.Yard;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class YardRepositoryTest {
    @Autowired private YardRepository repo;

    @Test
    public void testAddNew() {
        Yard yard = new Yard();
        
        yard.setYardName("MOMOMOMO");
        yard.setConcertOwnerID(100);
        Yard savedYard = repo.save(yard);

        Assertions.assertThat(savedYard).isNotNull();
        Assertions.assertThat(savedYard.getYardID()).isGreaterThan(0);

    }

    @Test 
    public void testListAll() {
       Iterable<Yard> yards = repo.findAll();
       Assertions.assertThat(yards).hasSizeGreaterThan(0);

       for (Yard yard : yards) {
            System.out.println(yard);
       }
    }

    @Test
    public void testUpdate() {
        Integer yardrId = 1;
        Optional<Yard> optionalYard = repo.findById(yardrId);
        Yard yard = optionalYard.get();
       
        repo.save(yard);

        Yard updatedYard = repo.findById(yardrId).get();
       
    }

    @Test
    public void testGet() {
        Integer yardrId = 2;
        Optional<Yard> optionalYard = repo.findById(yardrId);
        Assertions.assertThat(optionalYard).isPresent();
        System.out.println(optionalYard.get());
    }

    @Test
    public void testDelete() {
        Integer yardrId = 3;
        repo.deleteById(yardrId);
        Optional<Yard> optionalYard = repo.findById(yardrId);
        Assertions.assertThat(optionalYard).isNotPresent();


    }

}