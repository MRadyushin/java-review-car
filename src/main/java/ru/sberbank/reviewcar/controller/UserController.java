package ru.sberbank.reviewcar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.sberbank.reviewcar.config.TemplateUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static systems.Config.writeFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final CarService carService;
    private final UserService userService;
    private final EventService eventService;

    public static Map<String, Object> newparams = new HashMap();

    @SneakyThrows
    @GetMapping
    private Collection<User> getAllUsers() {

/**        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapper2 =      objectMapper.registerModule(new JavaTimeModule());
        objectMapper2.writeValue(new File("data2.json"),userService.getAllUsers());
        //writeFile("data2.json", objectMapper2.writeValueAsString(userService.getAllUsers()));
        Map<String, Object> params = new HashMap<String, Object>();
        DataInputStream dis =
                new DataInputStream (
                        new FileInputStream("data2.json"));

        byte[] datainBytes = new byte[dis.available()];
        dis.readFully(datainBytes);
        dis.close();

        String content = new String(datainBytes, 0, datainBytes.length);
        params.put("test",content);
        String templateName = "index2.html"; // имя темплейта

        String stringByTemplate = // в данную строку придет готовый темплейт составленный с значениями из Map<> params
                TemplateUtil.getStringByTemplate("steps/" + templateName, params); // в метод передается ссылка на темплейт который лежит в ресурсах и Map<> с значениями для темплейта

        writeFile("test2.html", stringByTemplate);*/

        return userService.getAllUsers();



    }

    @SneakyThrows
    @GetMapping(value = "/{id}")
    private User getUser(@PathVariable int id) {
       /** ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapper2 =      objectMapper.registerModule(new JavaTimeModule());
        objectMapper2.writeValue(new File("data2.json"),userService.getUser(id));
        //writeFile("data2.json", objectMapper2.writeValueAsString(userService.getAllUsers()));
        Map<String, Object> params = new HashMap<String, Object>();
        DataInputStream dis =
                new DataInputStream (
                        new FileInputStream("data2.json"));

        byte[] datainBytes = new byte[dis.available()];
        dis.readFully(datainBytes);
        dis.close();

        String content = new String(datainBytes, 0, datainBytes.length);
        params.put("test",content);
        String templateName = "index2.html"; // имя темплейта

        String stringByTemplate = // в данную строку придет готовый темплейт составленный с значениями из Map<> params
                TemplateUtil.getStringByTemplate("steps/" + templateName, params); // в метод передается ссылка на темплейт который лежит в ресурсах и Map<> с значениями для темплейта

        writeFile("test2.html", stringByTemplate);*/

        return userService.getUser(id);
    }

    @PostMapping
    private User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    private User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{userId}")
    public String deleteUser(@PathVariable int userId) {
        return userService.deleteUser(userId);
    }


    @GetMapping(value = "/{userId}/recommendations")
    private List<Car> getRecommendations(@PathVariable int userId) {
        return carService.getRecommendations(userId);
    }

    @GetMapping(value = "/{id}/feed")
    public List<Event> getFeed(@PathVariable("id") Integer id) {
        return eventService.getFeed(id);
    }
}

