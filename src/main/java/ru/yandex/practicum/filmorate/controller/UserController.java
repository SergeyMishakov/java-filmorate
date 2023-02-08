package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос создания пользователя");
        User checkedUser = checkUserBody(user);
        return userService.createUser(checkedUser);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException, AbsenceException {
        log.info("Получен запрос обновления данных пользователя");
        User checkedUser = checkUserBody(user);
        return userService.updateUser(checkedUser);
    }

    @GetMapping("/users")
    public ArrayList<User> getUserList() {
        log.info("Получен запрос получения списка пользователей");
        return userService.getUserList();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен запрос получения пользователя по идентификатору");
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос добавления в друзья");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос удаления из друзей");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public ArrayList<User> getFriendList(@PathVariable int id) {
        log.info("Получен запрос получения списка друзей пользователя");
        return userService.getFriendList(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ArrayList<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос получения списка друзей, общих с другими пользователями");
        return userService.getCommonFriends(id, otherId);
    }

    protected User checkUserBody(User user) throws ValidationException {
        //логин не может содержать пробелы;
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы");
            throw new ValidationException("Логин содержит пробелы");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if ((user.getName() == null) || (user.getName() == "")) {
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

