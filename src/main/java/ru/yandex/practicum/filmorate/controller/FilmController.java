package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@RestController
public class FilmController {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    //private final HashSet<Film> filmList = new HashSet<>();
    private final HashMap<Integer, Film> filmList = new HashMap<>();
    private int uniqueNumber = 1;

    //добавить фильм
    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        checkFilmBody(film);
        film.setId(uniqueNumber);
        filmList.put(uniqueNumber, film);
        uniqueNumber++;
        log.info("Получен запрос добавления фильма");
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        checkFilmBody(film);
        boolean isOK = false;
        for (int id : filmList.keySet()) {
            if (id == film.getId()) {
                isOK = true;
                break;
            }
        }
        if (!isOK) {
            throw new ValidationException("Попытка обновления несуществующего фильма!");
        }
        filmList.put(film.getId(), film);
        log.info("Получен запрос обновления фильма");
        return film;
    }

    @GetMapping("/films")
    public ArrayList<Film> getFilmList() {
        ArrayList<Film> films = new ArrayList<>();
        log.info("Получен запрос получения списка фильмов");
        for (Film film : filmList.values()) {
            films.add(film);
        }
        return films;
    }

    protected boolean checkFilmBody(Film film) throws ValidationException {
        //название не может быть пустым;
        if ((film.getName() == null) || film.getName() == "") {
            log.error("Пустое название!");
            throw new ValidationException("Пустое название!");
        }
        //максимальная длина описания — 200 символов;
        if (film.getDescription().length() > 200) {
            log.error("Превышена максимальная длина описания!");
            throw new ValidationException("Превышена максимальная длина описания!");
        }
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


