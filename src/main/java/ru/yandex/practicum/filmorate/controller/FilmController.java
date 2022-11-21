package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FilmController {

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    //добавить фильм
    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос добавления фильма");
        checkFilmBody(film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос обновления фильма");
        checkFilmBody(film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public ArrayList<Film> getFilmList() {
        log.info("Получен запрос получения списка фильмов");
        return filmService.getFilmList();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос добавления лайка фильму");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос удаления лайка у фильма");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilmList(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос получения списка фильмов");
        return filmService.getFilmRating(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен запрос получения ");
        return filmService.getFilmById(id);
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


