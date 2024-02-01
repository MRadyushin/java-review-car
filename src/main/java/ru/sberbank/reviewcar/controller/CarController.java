package ru.sberbank.reviewcar.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.service.CarService;
import systems.TemplateUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static steps.MurexVariables.nameFile5;
import static steps.MurexVariables.newparams;
import static systems.Config.writeFile;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {


    private final CarService carService;
// вывод всех машин
    @GetMapping
    private Collection<Car> getAllCars() {


        return carService.getAllCars();
    }
//поиск машины по id
    @GetMapping(value = "/{id}")
    private Car getCar(@PathVariable int id) {
        return carService.getCar(id);
    }
//список всех машин по рейтингу (поле rate)
    @GetMapping(value = "/popular")
    private Collection<Car> getPopularCars(@RequestParam(required = false, defaultValue = "10") int count,
                                           @RequestParam(required = false) Integer typeId,
                                           @RequestParam(required = false) Integer year) {
        return carService.getPopularCars(count, typeId, year);
    }
// добавить машину
    @PostMapping
    private Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }
// изменить машину
    @PutMapping
    private Car updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }
//удалить машину
    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable Integer id) {
        return carService.deleteCar(id);
    }
//поставить лайк на машину от юзера
    @PutMapping(value = "/{id}/like/{userId}")
    private void makeLike(@PathVariable int id, @PathVariable int userId) {
        carService.makeLike(id, userId);
    }
//удалить лайк с машины от юзера
    @DeleteMapping(value = "/{id}/like/{userId}")
    private void deleteLike(@PathVariable int id, @PathVariable int userId) {
        carService.deleteLike(id, userId);
    }

    @GetMapping("/director/{id}")
    public Collection<Car> getCarsByDirector(@PathVariable("id") Integer directorId
    ) {
        return carService.getCarsByDirector(directorId);
    }
//Машины которые лайкал юзер
    @GetMapping(value = "/like")
    private Collection<Car> getLikesCars(@RequestParam int userId) {
        return carService.getLikesCars(userId);
    }

    @GetMapping(value = "/search")
    private Collection<Car> getSearchCars(@RequestParam String query, @RequestParam String by) {
        return carService.getSearchCars(query, by);
    }
}

