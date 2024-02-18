package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.sberbank.reviewcar.dao.CarStorage;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.exception.ValidationException;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Klass;
import ru.sberbank.reviewcar.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CarServiceTest {

    private final UserService userService;
    private final CarService carService;
    private final CarStorage carStorage;

    @Test
    @DirtiesContext
    void getCar_withNormalBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Optional<Car> filmOptional = Optional.ofNullable(carService.getCar(carTest.getId()));

        assertTrue(filmOptional.isPresent());
        Car car = filmOptional.get();
        assertEquals(4, car.getId());
        assertEquals("name", car.getName());
        assertEquals("description", car.getDescription());
        assertEquals(LocalDate.of(2012, 1, 1), car.getReleaseDate());
        assertEquals(900000, car.getPrice());
        assertEquals(4, car.getRate());
    }

    @Test
    @DirtiesContext
    void getCar_withWrongId() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);
        int wrongId = 5;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            carService.getCar(wrongId);
        });

        assertEquals("Машина с id=" + wrongId + " не найдена", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void getAllCars_withNormalBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Car carTest1 = Car.builder()
                .id(5)
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(9000000)
                .rate(4)
                .klass(new Klass(8, "S")).build();
        carService.addCar(carTest1);

        Collection<Car> cars = carService.getAllCars();

        assertEquals(5, cars.size());
    }

    @Test
    @DirtiesContext
    void addCar_withNormalBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Optional<Car> filmOptional = Optional.ofNullable(carService.getCar(carTest.getId()));

        assertTrue(filmOptional.isPresent());
        Car car = filmOptional.get();
        assertEquals(4, car.getId());
        assertEquals("name", car.getName());
        assertEquals("description", car.getDescription());
        assertEquals(LocalDate.of(2012, 1, 1), car.getReleaseDate());
        assertEquals(900000, car.getPrice());
        assertEquals(4, car.getRate());
    }

    @Test
    @DirtiesContext
    void addCar_withEmptyName() {
        Car carTest = Car.builder()
                .id(4)
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            carService.addCar(carTest);
        });

        assertEquals("Название машины не может быть пустым.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void addCar_withLongDescription() {
        String symbols201 = "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОаывтлотлывалталоывалоыталоытвалыовывва" +
                "ывавытывтадылвадылвадылвоадлвыоадлвыоадлывадлывоадлвыыдвлаыдвлаодылвоадылваоыдлвал" +
                "алыовалыовалыоралвыораловыаыывоадлывоадлвыоадлвыоадлвыодалвыоадлвыоадлывоадлвыоады" +
                "аыщвоадылвоадлвыоадвылодвылоадывлоадвыдлвыадлывоадлывоадлывоадлыовадлвыодвылоадывл" +
                "дывоадлывоадлывоадыловадылводывлоадвылоаыдвладлвыадывлоадвылоадлвыоадвылоадылоадвы" +
                "ыжвлаоыдваодвылоадывлоадвылоадыааыовдаловыдлаоывпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯываываываываываы" +
                "ывавытывтадылвадылвадылвоадлвыоадлвыоадлывадлывоадлвыыдвлаыдвлаодылвоадылваоыдлвал" +
                "алыовалыовалыоралвыораловыаыывоадлывоадлвыоадлвыоадлвыодалвыоадлвыоадлывоадлвыоады" +
                "аыщвоадылвоадлвыоадвылодвылоадывлоадвыдлвыадлывоадлывоадлывоадлыовадлвыодвылоадывл" +
                "дывоадлывоадлывоадыловадылводывлоадвылоаыдвладлвыадывлоадвылоадлвыоадвылоадылоадвы" +
                "ыжвлаоыдваодвылоадывлоадвылоадыааыовдаловыдлаоывпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯываываываываываы" +
                "ывавытывтадылвадылвадылвоадлвыоадлвыоадлывадлывоадлвыыдвлаыдвлаодылвоадылваоыдлвал" +
                "алыовалыовалыоралвыораловыаыывоадлывоадлвыоадлвыоадлвыодалвыоадлвыоадлывоадлвыоады" +
                "аыщвоадылвоадлвыоадвылодвылоадывлоадвыдлвыадлывоадлывоадлывоадлыовадлвыодвылоадывл" +
                "дывоадлывоадлывоадыловадылводывлоадвылоаыдвладлвыадывлоадвылоадлвыоадвылоадылоадвы" +
                "ыжвлаоыдваодвылоадывлоадвылоадыааыовдаловыдлаоывпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯываываываываываы" +
                "ывавытывтадылвадылвадылвоадлвыоадлвыоадлывадлывоадлвыыдвлаыдвлаодылвоадылваоыдлвал" +
                "алыовалыовалыоралвыораловыаыывоадлывоадлвыоадлвыоадлвыодалвыоадлвыоадлывоадлвыоады" +
                "аыщвоадылвоадлвыоадвылодвылоадывлоадвыдлвыадлывоадлывоадлывоадлыовадлвыодвылоадывл" +
                "дывоадлывоадлывоадыловадылводывлоадвылоаыдвладлвыадывлоадвылоадлвыоадвылоадылоадвы" +
                "ыжвлаоыдваодвылоадывлоадвылоадыааыовдаловыдлаоывпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯываываываываываы" +
                "ывавытывтадылвадылвадылвоадлвыоадлвыоадлывадлывоадлвыыдвлаыдвлаодылвоадылваоыдлвал" +
                "алыовалыовалыоралвыораловыаыывоадлывоадлвыоадлвыоадлвыодалвыоадлвыоадлывоадлвыоады" +
                "аыщвоадылвоадлвыоадвылодвылоадывлоадвыдлвыадлывоадлывоадлывоадлыовадлвыодвылоадывл" +
                "дывоадлывоадлывоадыловадылводывлоадвылоаыдвладлвыадывлоадвылоадлвыоадвылоадылоадвы" +
                "ыжвлаоыдваодвылоадывлоадвылоадыааыовдаловыдлаоывпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯ" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯываываываываываы" +
                "аАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХцЦчЧшШщЩъЪыЫьЬэЭюЮяЯAAAываываываываыва";

        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description(symbols201)
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            carService.addCar(carTest);
        });

        assertEquals("Длина описания не может превышать больше 2000 символов.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void addCar_withNegativeDuration() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(-1)
                .rate(4)
                .klass(new Klass(9, "J")).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            carService.addCar(carTest);
        });

        assertEquals("Стоимость машины должна быть положительной.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void updateCar_withNormalBehavior() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Car carTest1 = Car.builder()
                .id(4)
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .price(800000)
                .rate(3)
                .klass(new Klass(3, "C")).build();
        carService.updateCar(carTest1);

        Optional<Car> filmOptional = Optional.ofNullable(carService.getCar(carTest.getId()));

        assertTrue(filmOptional.isPresent());
        Car car = filmOptional.get();
        assertEquals("new name", car.getName());
        assertEquals("new description", car.getDescription());
        assertEquals(LocalDate.of(2011, 1, 1), car.getReleaseDate());
        assertEquals(800000, car.getPrice());
        assertEquals(3, car.getRate());
    }

    @Test
    @DirtiesContext
    void updateCar_withWrongId() {
        Car carTest = Car.builder()
                .id(4)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();
        carService.addCar(carTest);

        Car carTest1 = Car.builder()
                .id(5)
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2012, 1, 1))
                .price(900000)
                .rate(4)
                .klass(new Klass(9, "J")).build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            carService.updateCar(carTest1);
        });

        assertEquals("Машина с id=" + carTest1.getId() + " не найдена", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void makeLike_withNormalBehavior() {
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
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 4, 3)).build();
        userService.createUser(userTest);

        carService.makeLike(carTest.getId(), userTest.getId());
        List<Integer> likes = carStorage.getCarLikes(carTest.getId());

        assertEquals(1, likes.size());
        assertEquals(userTest.getId(), likes.get(0));
    }

    @Test
    @DirtiesContext
    void deleteLike() {
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
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 4, 3)).build();
        userService.createUser(userTest);

        carService.makeLike(carTest.getId(), userTest.getId());
        List<Integer> likes = carStorage.getCarLikes(carTest.getId());

        assertEquals(1, likes.size());
        assertEquals(userTest.getId(), likes.get(0));

        carService.deleteLike(carTest.getId(), userTest.getId());

        List<Integer> likes1 = carStorage.getCarLikes(carTest.getId());

        assertTrue(likes1.isEmpty());
    }

    @Test
    @DirtiesContext
    void getPopularCars_withNormalBehavior() {
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
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 4, 3)).build();
        userService.createUser(userTest);
        carService.makeLike(carTest.getId(), userTest.getId());

        Collection<Car> cars = carService.getPopularCars(1, null, null);

        assertEquals(1, cars.size());
    }

    @Test
    @DirtiesContext
    void getPopularCars_withWrongCount() {
        int wrongCount = -1;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            carService.getPopularCars(wrongCount, null, null);
        });

        assertEquals("count не может быть отрицательным.", exception.getMessage());
    }
}
