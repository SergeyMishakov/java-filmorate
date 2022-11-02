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
import java.util.HashSet;
import java.util.Optional;

@RestController
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> filmList = new HashMap<>();
    private int uniqueNumber = 1;

    //добавить фильм
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос добавления фильма");
        checkFilmBody(film);
        film.setId(uniqueNumber);
        filmList.put(uniqueNumber, film);
        uniqueNumber++;
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос обновления фильма");
        checkFilmBody(film);
        if (!filmList.containsKey(film.getId())) {
            throw new ValidationException("Попытка обновления несуществующего фильма!");
        }
        filmList.put(film.getId(), film);
        return film;
    }

    @GetMapping("/films")
    public ArrayList<Film> getFilmList() {
        log.info("Получен запрос получения списка фильмов");
        ArrayList<Film> films = new ArrayList<>(filmList.values());
        return films;
    }

    protected boolean checkFilmBody(Film film) throws ValidationException {
        //дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года!");
            }
        //продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма отрицательная!");
            throw new ValidationException("Продолжительность фильма не положительная!");
        }
        return true;
    }
}


