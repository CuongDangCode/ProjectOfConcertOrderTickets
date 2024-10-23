// package com.example;




// import java.util.Optional;

// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.annotation.Rollback;

// import com.example.CRUD.Repository.AreaRepository;
// import com.example.mo.Area;







// @DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Rollback(false)
// public class AreaRepositoryTest {
//     @Autowired private AreaRepository repo;

//     @Test
//     public void testAddNew() {
//         Area area = new Area();
        
//         Area.setRoomname("VIP");
//         Area savedArea = repo.save(area);

//         Assertions.assertThat(savedArea).isNotNull();
//         Assertions.assertThat(savedArea.getAreaID()).isGreaterThan(0);

//     }

//     @Test 
//     public void testListAll() {
//        Iterable<Area> areas = repo.findAll();
//        Assertions.assertThat(areas).hasSizeGreaterThan(0);

//        for (Area area : areas) {
//             System.out.println(area);
//        }
//     }


//     @Test
//     public void testUpdate() {
//         Integer areaId = 3;
//         Optional<Area> optionalArea = repo.findById(areaId);
//         Area area = optionalArea.get();
//         area.setRoomname("VIP");;
//         repo.save(area);

//         Area updatedArea = repo.findById(areaId).get();
//         Assertions.assertThat(updatedArea.getRoomname()).isEqualTo("VIP");
//     }
    
//     @Test
//     public void testGet() {
//         Integer areaId = 3;
//         Optional<Area> optionalArea = repo.findById(areaId);
//         Assertions.assertThat(optionalArea).isPresent();
//         System.out.println(optionalArea.get());
//     }

//      @Test
//     public void testDelete() {
//         Integer areaId = 3;
//         repo.deleteById(areaId);
//         Optional<Area> optionalArea = repo.findById(areaId);
//         Assertions.assertThat(optionalArea).isNotPresent();


//     }






// }