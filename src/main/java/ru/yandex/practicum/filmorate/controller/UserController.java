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
        //электронная почта не может быть пустой и должна содержать символ @;
        if  (user.getEmail() == null) {
            log.error("Электронная почта пустая или не содержит символ @!");
            throw new ValidationException("Электронная почта пустая или не содержит символ @!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Электронная почта пустая или не содержит символ @!");
            throw new ValidationException("Электронная почта пустая или не содержит символ @!");
        }
        //логин не может быть пустым и содержать пробелы;
        if ((user.getLogin() == null) || (user.getLogin().contains(" "))) {
            log.error("Логин пустой или содержит пробелы");
            throw new ValidationException("Логин пустой или содержит пробелы");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null) {
            log.warn("Имя не заполнено - будет использован логин");
            user.setName(user.getLogin());
        }
        //дата рождения не может быть в будущем.
        Optional<LocalDate> birthdayOptional = Optional.of(user.getBirthday());
        if (birthdayOptional.isPresent()) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения в будущем!");
                throw new ValidationException("Дата рождения в будущем!");
            }
        }

        return user;
    }
}

