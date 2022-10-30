package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    FilmController filmController = new FilmController();

    //проверка корректного фильма
    @Test
    void checkGoodFilm() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2012, 10, 10))
                .duration(120)
                .build();
        boolean result = filmController.checkFilmBody(film);
        assertTrue(result);
    }

    //проверка фильма с пустым названием
    @Test
    void checkFilmWithoutName() throws ValidationException {
        Film film = Film.builder()
                .name("")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2012, 10, 10))
                .duration(120)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }

    //максимальная длина описания — 200 символов;
    @Test
    void checkFilmWithDescriptionMore200() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм1")
                .description("Оооооооооооооооооооччччччччччччччччччччччччччччеееееееееееееееееееееееееен" +
                        "нннннннььььььььььььььь дддддддддддддддлллллллллллллллиииииииииииииннннннннннннннннноо" +
                        "ооооооооооооооооооеееееееееееееееееееее Описание фильма 111111111111111111111111111" +
                        "111111111111111111111111111111111111111111111111111111111111111111111111111")
                .releaseDate(LocalDate.of(2012, 10, 10))
                .duration(120)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }

    //дата релиза — не раньше 28 декабря 1895 года;
    @Test
    void checkFilmWithReleaseBefore1895() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(1700, 10, 10))
                .duration(120)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }

    //продолжительность фильма должна быть положительной (отрицательная)
    @Test
    void checkFilmWithNegativeDuration() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(-120)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }

    //продолжительность фильма должна быть положительной (нулевая)
    @Test
    void checkFilmWithNullDuration() throws ValidationException {
        Film film = Film.builder()
                .name("Фильм1")
                .description("Описание фильма 1")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(0)
                .build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }

    //пустой фильм
    @Test
    void checkEmptyFilm() throws ValidationException {
        Film film = Film.builder().build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        boolean result = filmController.checkFilmBody(film);
                    }
                });
    }
}
