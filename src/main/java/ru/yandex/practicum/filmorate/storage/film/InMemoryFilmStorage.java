package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer, Film> filmList = new HashMap<>();
    private int uniqueNumber = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(uniqueNumber);
        filmList.put(uniqueNumber, film);
        uniqueNumber++;
        return film;
    }

    @Override
    public HashMap<Integer, Film> getFilmList() {
        return filmList;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmList.containsKey(film.getId())) {
            try {
                throw new AbsenceException("Фильм не найден!");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        filmList.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (!filmList.containsKey(id)) {
            try {
                throw new AbsenceException("Фильм не найден!");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        } else {
            return filmList.get(id);
        }
    }
}
