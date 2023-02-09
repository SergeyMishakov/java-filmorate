package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

public interface FilmStorage {

    HashMap<Long, Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    List<Genre> getGenreList();

    Genre getGenreById(int id);

    List<Mpa> getMPAList();

    Mpa getMPAById(int id);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Long> getLikesByFilm(long filmId);
}
