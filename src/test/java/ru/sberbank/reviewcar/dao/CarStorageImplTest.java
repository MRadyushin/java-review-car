package ru.sberbank.reviewcar.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.sberbank.reviewcar.controller.TypeController;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.model.Type;
import ru.sberbank.reviewcar.model.User;
import ru.sberbank.reviewcar.service.CarService;
import ru.sberbank.reviewcar.service.TypeService;
import ru.sberbank.reviewcar.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CarStorageImplTest {

    private final UserService userService;
    private final CarService carService;
    private final CarStorage carStorage;
    private final TypeService type;

    @Test
    @DirtiesContext
    void getCarLikes_withNormanBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        User userTest = User.builder()
                .id(4)
                .email("test@mail.ru")
                .login("test")
                .name("Test")
                .birthday(LocalDate.of(1996, 8, 9)).build();
        userService.createUser(userTest);
        carService.makeLike(carTest.getId(), userTest.getId());

        List<Integer> likes = carStorage.getCarLikes(carTest.getId());

        assertEquals(1, likes.size());
        assertEquals(userTest.getId(), likes.get(0));
    }

    @Test
    @DirtiesContext
    void checkKlass_withNormalBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Klass klass = carStorage.checkKlass(carTest);

        assertEquals(9, klass.getId());
        assertEquals("J", klass.getName());
    }


    @Test
    @DirtiesContext
    void checkKlass_withWrongMpaId() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(10, "J")).build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> carService.addCar(carTest));

        assertEquals("Не найден Klass с id: 10", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void checkType_withNormalBehavior() {
        List<Type> types = new ArrayList<>();
        Type type = new Type(1, "Универсал");
        types.add(type);
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J"))
                .types(types).build();
        carService.addCar(carTest);

        List<Type> typeList = carStorage.checkType(carTest);

        assertEquals(1, typeList.size());
        Type type1 = typeList.get(0);
        assertEquals(1, type1.getId());
        assertEquals("Универсал", type1.getName());
    }

    @Test
    @DirtiesContext
    void checkType_withWrongTypeId() {
        List<Type> types = new ArrayList<>();
        Type type = new Type(10, "нечтоНепонятное");
        types.add(type);
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J"))
                .types(types).build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> carService.addCar(carTest));

        assertEquals("Не найден Type с id: 10", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void insertCarTypes_withNormalBehavior() {
        List<Type> types = new ArrayList<>();
        Type type1 = type.getTypeById(1);
        Type type2 = type.getTypeById(2);
        Type type3 = type.getTypeById(3);
        types.add(type1);
        types.add(type2);
        types.add(type3);
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J"))
                .types(types).build();
        carStorage.addCar(carTest);
        carTest.setTypes(types);
        carStorage.insertCarTypes(carTest);

        List<Type> typeList = carStorage.getTypes(carTest.getId());

        assertEquals(3, typeList.size());
    }

    @Test
    @DirtiesContext
    void deleteCarTypes_withNormalBehavior() {
        List<Type> types = new ArrayList<>();
        Type type = new Type(1, "Универсал");
        Type type1 = new Type(2, "Седан");
        types.add(type);
        types.add(type1);
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J"))
                .types(types).build();
        carStorage.addCar(carTest);
        carTest.setTypes(types);
        carStorage.insertCarTypes(carTest);

        List<Type> typeList = carStorage.getTypes(carTest.getId());

        assertEquals(2, typeList.size());

        carStorage.deleteCarTypes(carTest);
        List<Type> typeList1 = carStorage.getTypes(carTest.getId());

        assertEquals(0, typeList1.size());
    }

    @Test
    @DirtiesContext
    void deleteCar_withNormalBehavior() {
        Car car1 = Car.builder().name("car1").description("desc1").releaseDate(LocalDate.of(1990, 1,
                1)).types(List.of()).rate(0).price(500000).klass(Klass.builder().id(1).name("A").build()).build();
        carStorage.addCar(car1);
        List<Car> carsBeforeDelete = new ArrayList<>(carStorage.getAllCars());
        assertEquals(4, carsBeforeDelete.size());

        carStorage.deleteCar(1);
        List<Car> carsAfterDelete = new ArrayList<>(carStorage.getAllCars());
        assertEquals(3, carsAfterDelete.size());
    }
}
