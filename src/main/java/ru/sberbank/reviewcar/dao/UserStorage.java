package ru.sberbank.reviewcar.dao;

import ru.sberbank.reviewcar.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User getUser(int id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    String deleteUser(int userId);

    List<Integer> getRecommendations(int userId,User targetUser);
}
