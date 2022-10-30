package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();

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

    //электронная почта не может быть пустой
    @Test
    void checkUserWithNullEmail() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .name("Afanasiy Arkadievich")
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

    //электронная почта должна содержать символ @;
    @Test
    void checkUserWithEmailWithoutDog() throws ValidationException {
        User user = User.builder()
                .login("Truboprokatchic")
                .name("Afanasiy Arkadievich")
                .email("truboprokatchikyandex.ru")
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

    //логин не может быть пустым
    @Test
    void checkUserWithoutLogin() throws ValidationException {
        User user = User.builder()
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

    //пустой юзер
    @Test
    void checkEmptyUser() throws ValidationException {
        User user = User.builder().build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User resultUser = userController.checkUserBody(user);
                    }
                });
    }
}