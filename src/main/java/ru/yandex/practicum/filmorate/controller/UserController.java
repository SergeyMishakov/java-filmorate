package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> userList = new HashMap<>();

    private int uniqueNumber = 1;

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос создания пользователя");
        User checkedUser = checkUserBody(user);
        checkedUser.setId(uniqueNumber);
        userList.put(uniqueNumber, checkedUser);
        uniqueNumber++;
        return checkedUser;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос обновления данных пользователя");
        User checkedUser = checkUserBody(user);
        if (!userList.containsKey(user.getId())) {
            throw new ValidationException("Попытка обновления несуществующего пользователя!");
        }
        userList.put(checkedUser.getId(), checkedUser);
        return checkedUser;
    }

    @GetMapping("/users")
    public ArrayList<User> getUserList() {
        log.info("Получен запрос обновления фильма");
        ArrayList<User> users = new ArrayList<>(userList.values());
        return users;
    }

    protected User checkUserBody(User user) throws ValidationException {
        //логин не может содержать пробелы;
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы");
            throw new ValidationException("Логин содержит пробелы");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null) {
            log.warn("Имя не заполнено - будет использован логин");
            user.setName(user.getLogin());
        }
        //дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения в будущем!");
            throw new ValidationException("Дата рождения в будущем!");
        }
        return user;
    }
}

