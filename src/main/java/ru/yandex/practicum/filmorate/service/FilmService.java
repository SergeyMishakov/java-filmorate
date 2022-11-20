package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    PopularityComparator popularityComparator = new PopularityComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //добавить фильм
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    //обновить фильм
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    //вернуть весь список фильмов
    public ArrayList<Film> getFilmList() {
        ArrayList<Film> filmList = new ArrayList<>(filmStorage.getFilmList().values());
        return filmList;
    }

    //вернуть фильм по идентификатору
    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    //поставить лайк
    public void addLike(int filmId, long userId) {
        Film film = getFilmById(filmId);
        film.addLike(userId);
        updateFilm(film);
    }

    //удалить лайк
    public void deleteLike(int filmId, long userId) {
        Film film = getFilmById(filmId);
        film.deleteLike(userId);
        updateFilm(film);
    }

    //вернуть самые популярные фильмы
    public List<Film> getFilmRating(int count) {
        List<Film> filmList = getFilmList();
        filmList.sort(popularityComparator);
        List<Film> popularFilms = new ArrayList<>();
        if (filmList.size() < count) {
            count = filmList.size();
        }
        for (int i = 0; i < count; i++) {
            popularFilms.add(filmList.get(i));
        }
        return popularFilms;
    }

    public class PopularityComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            return film2.getLikeList().size() - film1.getLikeList().size();
        }
    }
}
