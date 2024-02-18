package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.exception.ValidationException;
import ru.sberbank.reviewcar.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;

    @Test
    @DirtiesContext
    void getUser_withNormalBehavior() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);

        Optional<User> userOptional = Optional.ofNullable(userService.getUser(userTest.getId()));

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(4, user.getId());
        assertEquals("maksim2@mail.ru", user.getEmail());
        assertEquals("maksim2", user.getLogin());
        assertEquals("Maksim2", user.getName());
        assertEquals(LocalDate.of(1995, 1, 16), user.getBirthday());
    }

    @Test
    @DirtiesContext
    void getAllUsers_withNormalBehavior() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);
        User userTest1 = User.builder()
                .id(5)
                .email("maksim3@mail.ru")
                .login("maksim3")
                .name("Maksim3")
                .birthday(LocalDate.of(1995, 1, 17)).build();
        userService.createUser(userTest1);

        Collection<User> users = userService.getAllUsers();

        assertEquals(5, users.size());
        assertEquals("maksim2@mail.ru", userTest.getEmail());
        assertEquals("maksim2", userTest.getLogin());
        assertEquals("Maksim2", userTest.getName());
        assertEquals(LocalDate.of(1995, 1, 16), userTest.getBirthday());
        assertEquals("maksim3@mail.ru", userTest1.getEmail());
        assertEquals("maksim3", userTest1.getLogin());
        assertEquals("Maksim3", userTest1.getName());
        assertEquals(LocalDate.of(1995, 1, 17), userTest1.getBirthday());
    }

    @Test
    @DirtiesContext
    void createUser_withNormalBehavior() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);

        Optional<User> userOptional = Optional.ofNullable(userService.getUser(userTest.getId()));

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(4, user.getId());
        assertEquals("maksim2@mail.ru", user.getEmail());
        assertEquals("maksim2", user.getLogin());
        assertEquals("Maksim2", user.getName());
        assertEquals(LocalDate.of(1995, 1, 16), user.getBirthday());
    }

    @Test
    @DirtiesContext
    void createUser_withEmptyName() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);

        Optional<User> userOptional = Optional.ofNullable(userService.getUser(userTest.getId()));

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(4, user.getId());
        assertEquals("maksim2@mail.ru", user.getEmail());
        assertEquals("maksim2", user.getLogin());
        assertEquals("Maksim2", user.getName());
        assertEquals(LocalDate.of(1995, 1, 16), user.getBirthday());
    }

    @Test
    @DirtiesContext
    void createUser_withWrongEmail() {
        User userTest = User.builder()
                .id(4)
                .email("")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.createUser(userTest));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void createUser_withEmptyLogin() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login(" ")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.createUser(userTest));

        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void updateUser_withNormalBehavior() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);

        User userTest1 = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim3")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.updateUser(userTest1);

        Optional<User> userOptional = Optional.ofNullable(userService.getUser(userTest.getId()));

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals("Maksim3", user.getName());
    }

    @Test
    @DirtiesContext
    void updateUser_withWrongId() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);

        User userTest1 = User.builder()
                .id(5)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim3")
                .birthday(LocalDate.of(1995, 1, 16)).build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.updateUser(userTest1));

        assertEquals("User с id " + userTest1.getId() + " не найден.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    void deleteUser_withNormalBehavior() {
        User userTest = User.builder()
                .id(4)
                .email("maksim2@mail.ru")
                .login("maksim2")
                .name("Maksim2")
                .birthday(LocalDate.of(1995, 1, 16)).build();
        userService.createUser(userTest);
        List<User> usersBeforeDelete = new ArrayList<>(userService.getAllUsers());
        assertEquals(4, usersBeforeDelete.size());

        userService.deleteUser(4);
        List<User> usersAfterDelete = new ArrayList<>(userService.getAllUsers());
        assertEquals(3, usersAfterDelete.size());
    }
}