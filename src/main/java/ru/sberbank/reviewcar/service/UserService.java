package ru.sberbank.reviewcar.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.reviewcar.exception.NotFoundException;
import ru.sberbank.reviewcar.validator.UserValidator;
import ru.sberbank.reviewcar.dao.UserStorage;
import ru.sberbank.reviewcar.model.User;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorageDb;
    private final EventService eventService;

    public Collection<User> getAllUsers() {
        return userStorageDb.getAllUsers();
    }

    public User getUser(int id) {
        User user = userStorageDb.getUser(id);
        if (user == null) {
            throw new NotFoundException("User с id " + id + " не найден.");
        }
        return user;
    }

    public User createUser(User user) {
        UserValidator.validateUser(user);
        return userStorageDb.createUser(user);
    }

    public User updateUser(User user) {
        UserValidator.validateUser(user);
        getUser(user.getId());
        return userStorageDb.updateUser(user);
    }

    public String deleteUser(int userId) {
        return userStorageDb.deleteUser(userId);
    }
}
