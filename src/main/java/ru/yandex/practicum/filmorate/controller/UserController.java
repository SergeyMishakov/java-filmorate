package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> userList = new HashMap<>();

    private int uniqueNumber = 1;

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        User checkedUser = checkUserBody(user);
        checkedUser.setId(uniqueNumber);
        userList.put(uniqueNumber, checkedUser);
        uniqueNumber++;
        log.info("Получен запрос создания пользователя");
        return checkedUser;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        User checkedUser = checkUserBody(user);
        boolean isOK = false;
        for (int id : userList.keySet()) {
            if (id == checkedUser.getId()) {
                isOK = true;
                break;
            }
        }
        if (!isOK) {
            throw new ValidationException("Попытка обновления несуществующего пользователя!");
        }
        userList.put(checkedUser.getId(), checkedUser);
        log.info("Получен запрос обновления данных пользователя");
        return checkedUser;
    }

    @GetMapping("/users")
    public ArrayList<User> getUserList() {
        log.info("Получен запрос обновления фильма");
        ArrayList users = new ArrayList<>();
        for (User user : userList.values()) {
            users.add(user);
        }
        return users;
    }

    protected User checkUserBody(User user) throws ValidationException {
        //электронная почта не может быть пустой и должна содержать символ @;
        if  (user.getEmail() == null) {
            //if ((!user.getEmail().contains("@")) || (user.getEmail() == null)) {
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
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения в будущем!");
            throw new ValidationException("Дата рождения в будущем!");
        }
        return user;
    }
}

