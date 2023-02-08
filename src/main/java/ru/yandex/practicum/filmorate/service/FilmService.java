package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    PopularityComparator popularityComparator = new PopularityComparator();

    //с помощью аннотации Qualifier выбираем какой бин использовать для хранения фильмов
    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //добавить фильм
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    //обновить фильм
    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) != null) {
            return filmStorage.updateFilm(film);
        } else {
            try {
                throw new AbsenceException("Такого фильма нет");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }

    }

    //вернуть весь список фильмов
    public ArrayList<Film> getFilmList() {
        ArrayList<Film> filmList = new ArrayList<>(filmStorage.getFilmList().values());
        return filmList;
    }

    //вернуть фильм по идентификатору
    public Film getFilmById(int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            try {
                throw new AbsenceException("Такого фильма нет");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        return film;
    }

    //поставить лайк
    public void addLike(int filmId, long userId) {
        filmStorage.addLike(filmId, userId);
    }

    //удалить лайк
    public void deleteLike(int filmId, int userId) {
        List<Integer> likeList = filmStorage.getLikesByFilm(filmId);
        if (likeList.contains(userId)) {
            filmStorage.deleteLike(filmId, userId);
        } else {
            try {
                throw new AbsenceException("Пользователь не ставил лайк этому фильму");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
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

    //вернуть список всех жанров
    public List<Genre> getGenreList() {
        return filmStorage.getGenreList();
    }

    //вернуть жанр по id
    public Genre getGenreById(int id) {
        if (filmStorage.getGenreById(id) == null) {
            try {
                throw new AbsenceException("Такого жанра нет");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        return filmStorage.getGenreById(id);
    }

    //получить список всех рейтингов
    public List<Mpa> getMPAList() {
        return filmStorage.getMPAList();
    }

    //вернуть рейтинг по id
    public Mpa getMPAById(int id) {
        if (filmStorage.getMPAById(id) == null) {
            try {
                throw new AbsenceException("Такого MPA нет");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        return filmStorage.getMPAById(id);
    }
    public class PopularityComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            return film2.getLikeList().size() - film1.getLikeList().size();
        }
    }
}
