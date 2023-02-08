package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    //проверка корректного юзера
    @Test
    void checkGoodUser() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .name("Afanasiy Arkadievich")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.of(1992, 4, 21))
                .build();
        User resultUser = userController.checkUserBody(user);
        assertEquals(resultUser, user);
    }

    //логин не может содержать пробелы;
    @Test
    void checkUserWith_InLogin() throws ValidationException {
        User user = User.builder()
                .login("Trubopr okatchic")
                .name("Afanasiy Arkadievich")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.of(1992, 4, 21))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User resultUser = userController.checkUserBody(user);
                    }
                });
    }

    //имя для отображения может быть пустым — в таком случае будет использован логин;
    @Test
    void checkUserWithoutName() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.of(1992, 4, 21))
                .build();
        User resultUser = userController.checkUserBody(user);
        User userAfterCheck = User.builder()
                .login("Truboprokatchic")
                .name("Truboprokatchic")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.of(1992, 4, 21))
                .build();
        assertEquals(resultUser, userAfterCheck);
    }

    //дата рождения не может быть в будущем.
    @Test
    void checkUserWithBirthdayInFuture() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .name("Afanasiy Arkadievich")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.of(2050, 4, 21))
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User resultUser = userController.checkUserBody(user);
                    }
                });
    }

    //дата рождения равна текущей дате
    @Test
    void checkUserWithBirthdayEqualsToday() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .name("Afanasiy Arkadievich")
                .email("truboprokatchik@yandex.ru")
                .birthday(LocalDate.now())
                .build();
        User resultUser = userController.checkUserBody(user);
        assertEquals(resultUser, user);
    }
}