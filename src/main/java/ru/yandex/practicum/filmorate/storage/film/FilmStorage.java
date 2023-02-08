package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

public interface FilmStorage {

    HashMap<Integer, Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    List<Genre> getGenreList();

    Genre getGenreById(int id);

    List<Mpa> getMPAList();

    Mpa getMPAById(int id);

    void addLike(int filmId, long userId);

    void deleteLike(int filmId, long userId);

    List<Integer> getLikesByFilm(int filmId);
}
