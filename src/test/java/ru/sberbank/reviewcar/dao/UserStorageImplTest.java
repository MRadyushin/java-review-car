package ru.sberbank.reviewcar.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.model.Type;
import ru.sberbank.reviewcar.model.User;
import ru.sberbank.reviewcar.service.CarService;
import ru.sberbank.reviewcar.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserStorageImplTest {
    private final UserService userService;
    private final CarService carService;

    @Test
    void getCommonCars_withNormalBehavior() {
        User user1 = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        User user2 = User.builder()
                .login("friend")
                .name("friend adipisicing")
                .email("friend@mail.ru")
                .birthday(LocalDate.of(1976, 8, 20))
                .build();

        User user3 = User.builder()
                .login("common")
                .name("friend adipisicing")
                .email("friend@common.ru")
                .birthday(LocalDate.of(2000, 8, 20))
                .build();

        Car car1 = Car.builder()
                .name("tutu")
                .description("tutu")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .price(100)
                .klass(new Klass(1, null))
                .build();

        Car car2 = Car.builder()
                .name("New cars")
                .description("New car")
                .releaseDate(LocalDate.of(1999, 4, 30))
                .price(1200000)
                .klass(new Klass(3, "C"))
                .types(new ArrayList<>(List.of(new Type(1, "Универсал"))))
                .build();

        Car car3 = Car.builder()
                .name("New cars2")
                .description("New car2")
                .releaseDate(LocalDate.of(1999, 4, 30))
                .price(120)
                .klass(new Klass(3, "C"))
                .types(new ArrayList<>(List.of(new Type(1, "Универсал"))))
                .build();

        List<Car> cars = List.of(car1, car2, car3);
        List<User> users = List.of(user1, user2, user3);
        cars.forEach(carService::addCar);
        users.forEach(userService::createUser);
        carService.makeLike(5, 4);
        carService.makeLike(5, 5);
        carService.makeLike(4, 5);
        carService.makeLike(3, 6);
        carService.makeLike(2, 6);
        carService.makeLike(1, 6);
        Optional<List<Car>> optionalRecommendations = Optional.ofNullable(carService.getRecommendations(4));
        assertThat(optionalRecommendations)
                .isPresent()
                .hasValueSatisfying(filmsFromDb -> assertThat(filmsFromDb.get(0).getName().equals("tutu")).isTrue());
    }

}
