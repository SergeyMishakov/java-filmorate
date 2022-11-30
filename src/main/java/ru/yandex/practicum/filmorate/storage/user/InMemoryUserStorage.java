package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> userList = new HashMap<>();

    private int uniqueNumber = 1;

    @Override
    public User createUser(User user) {
        user.setId(uniqueNumber);
        userList.put(uniqueNumber, user);
        uniqueNumber++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userList.containsKey(user.getId())) {
            try {
                throw new AbsenceException("Попытка обновления несуществующего пользователя!");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public HashMap<Integer, User> getUserList() {
        return userList;
    }

    @Override
    public User getUserById(int id) {
        if (!userList.containsKey(id)) {
            try {
                throw new AbsenceException("Пользователь не найден!");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        } else {
            return userList.get(id);
        }
    }
}
