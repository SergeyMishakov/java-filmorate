package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    HashMap<Integer, User> getUserList();

    User getUserById(int id);
}
