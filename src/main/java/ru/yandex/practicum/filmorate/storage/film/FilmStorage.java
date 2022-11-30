package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {

    HashMap<Integer, Film> getFilmList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);
}
