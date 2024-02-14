package ru.sberbank.reviewcar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.service.CarService;

import java.util.Collection;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    /**
     * @return - возвращает весь список машин
     */
    @GetMapping
    private Collection<Car> getAllCars() {


        return carService.getAllCars();
    }

    /**
     * @param id - id машины
     *
     * @return - возвращает машину по id
     */
    @GetMapping(value = "/{id}")
    private Car getCar(@PathVariable int id) {
        return carService.getCar(id);
    }

    /**
     * @param count  - количество выводимых машин
     * @param typeId - тип машины
     * @param year   - год выпуска
     *
     * @return - выводит список всех машин по рейтингу (поле rate)
     */
    @GetMapping(value = "/popular")
    private Collection<Car> getPopularCars(@RequestParam(required = false, defaultValue = "10") int count, @RequestParam(required = false) Integer typeId, @RequestParam(required = false) Integer year) {
        return carService.getPopularCars(count, typeId, year);
    }

    /**
     * Метод создания машины
     */
    @PostMapping
    private Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    /**
     * Метод изменения машины
     */
    @PutMapping
    private Car updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    /**
     * Метод удаления машины
     */
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable Integer id) {
        return carService.deleteCar(id);
    }

    /**
     * Метод установки лайка на машину
     *
     * @param id     - id машины
     * @param userId - id пользователя
     */
    @PutMapping(value = "/{id}/like/{userId}")
    private void makeLike(@PathVariable int id, @PathVariable int userId) {
        carService.makeLike(id, userId);
    }

    /**
     * Метод удаления лайка на машину
     *
     * @param id     - id машины
     * @param userId - id пользователя
     */
    @DeleteMapping(value = "/{id}/like/{userId}")
    private void deleteLike(@PathVariable int id, @PathVariable int userId) {
        carService.deleteLike(id, userId);
    }

    /**
     * @param userId - id пользователя
     *
     * @return - возвращает машины,которые лайкал пользователь
     */
    @GetMapping(value = "/like")
    private Collection<Car> getLikesCars(@RequestParam int userId) {
        return carService.getLikesCars(userId);
    }

    /**
     * @return - метод поиска машины
     */
    @GetMapping(value = "/search")
    private Collection<Car> getSearchCars(@RequestParam String query, @RequestParam String by) {
        return carService.getSearchCars(query, by);
    }
}

