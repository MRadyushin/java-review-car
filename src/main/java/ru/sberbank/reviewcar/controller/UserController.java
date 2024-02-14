package ru.sberbank.reviewcar.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.reviewcar.model.Car;
import ru.sberbank.reviewcar.model.Event;
import ru.sberbank.reviewcar.model.User;
import ru.sberbank.reviewcar.service.CarService;
import ru.sberbank.reviewcar.service.EventService;
import ru.sberbank.reviewcar.service.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final CarService carService;
    private final UserService userService;
    private final EventService eventService;

    public static Map<String, Object> newparams = new HashMap();

    /**
     *
     * @return возвращает всех пользователей
     */
    @SneakyThrows
    @GetMapping
    private Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     *
     * @param id - id пользвателя
     * @return - возвращает пользователя по id
     */
    @SneakyThrows
    @GetMapping(value = "/{id}")
    private User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    /**
     *
     * создание пользователя
     */
    @PostMapping
    private User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     *
     * изменение пользователя
     */
    @PutMapping
    private User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     *
     * удаление пользователя
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{userId}")
    public String deleteUser(@PathVariable int userId) {
        return userService.deleteUser(userId);
    }

    /**
     *
     * рекомендации машин для пользователя
     */
    @GetMapping(value = "/{userId}/recommendations")
    private List<Car> getRecommendations(@PathVariable int userId) {
        return carService.getRecommendations(userId);
    }

    /**
     *
     * список действий пользователя
     */
    @GetMapping(value = "/{id}/feed")
    public List<Event> getFeed(@PathVariable("id") Integer id) {
        return eventService.getFeed(id);
    }
}

