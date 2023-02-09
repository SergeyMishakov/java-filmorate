package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //получить список всех фильмов -> SELECT * FROM film
    @Override
    public HashMap<Long, Film> getFilmList() {
        String sql = "SELECT * FROM FILMS";
        HashMap<Long, Film> filmList = new HashMap<>();
        List<Film> filmArrayList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        for (Film film: filmArrayList) {
            //дополняем объект данными по MPA, жанрам и лайкам
            film.setMpa(getMPAById(film.getMpa().getId()));
            Film filmWithGenre = addGenresToFilm(film);
            Film filmWithLikes = addLikesToFilm(filmWithGenre);
            filmList.put(filmWithLikes.getId(), filmWithLikes);
        }
        return filmList;
    };

    //создание объекта фильма
    private static Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("MPA_id"))
        );
    }

    //получить фильм по ИД
    @Override
    public Film getFilmById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", id);
        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new Mpa(filmRows.getInt("MPA_id"))
            );
            film.setMpa(getMPAById(film.getMpa().getId()));
            Film filmWithGenre = addGenresToFilm(film);
            Film filmWithLikes = addLikesToFilm(filmWithGenre);
            log.info("Обработан запрос получения фильма {}", filmRows.getString("name"));
            return filmWithLikes;
        } else {
            log.info("Фильм не найден");
            return null;
        }
    };

    //добавить фильм
    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        List<Genre> genreList = film.getGenres();
        if (genreList != null) {
            for(Genre genre : genreList) {
                String sqlGenreQuery = "INSERT INTO genre_of_film (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sqlGenreQuery, id, genre.getId());
            }
        }
        return getFilmById(id);
    };


    //обновить фильм
    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, MPA_id = ?" + "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        //удалить старые жанры фильма
        String sqlDeleteGenreQuery = "DELETE FROM genre_of_film WHERE film_id = ?";
        jdbcTemplate.update(sqlDeleteGenreQuery, film.getId());
        //добавить новые жанры фильма
        List<Genre> genreList = film.getGenres();
        //удалить дубликаты жанров из входящих данных
        List<Genre> resultListGenre = deleteDublicates(genreList);
        for(Genre genre : resultListGenre) {
            String sqlLoadGenreQuery = "INSERT INTO genre_of_film (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlLoadGenreQuery, film.getId(), genre.getId());
        }
        return getFilmById(film.getId());
    };

    public List<Genre> deleteDublicates (List<Genre> genreList) {
        Set<Genre> genreSet = new HashSet<>();
        for (Genre genre : genreList) {
            genreSet.add(genre);
        }
        List<Genre> resultListGenre = new ArrayList<>();
        for (Genre genre : genreSet) {
            resultListGenre.add(genre);
        }
        return resultListGenre;
    }

    //получить список жанров фильма
    public Film addGenresToFilm(Film film) {
        String sql = "SELECT gof.genre_id AS genre_id, " +
                            "g.genre_name AS genre_name " +
                "      FROM genre_of_film AS gof JOIN genre AS g ON gof.genre_id = g.genre_id " +
                "      WHERE gof.film_id = ?";
        List<Genre> genreArrayList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId());
        if (!genreArrayList.isEmpty()) {
            for (Genre genre: genreArrayList) {
                film.setGenres(genre);
            }
        }
        return film;
    }

    //проставить лайки
    public Film addLikesToFilm(Film film) {
        List<Long> likeList = getLikesByFilm(film.getId());
        for (Long like : likeList) {
            film.addLike(like);
        }
        return film;
    }

    //вернуть список всех жанров
    @Override
    public List<Genre> getGenreList() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    //вернуть жанр по id
    @Override
    public Genre getGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where genre_id = ?", id);
        if(genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("genre_name")
            );
            log.info("Обработан запрос получения жанра {}", genreRows.getString("genre_name"));
            return genre;
        } else {
            return null;
        }
    }

    //создать объект жанра
    private static Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        );
    }

   //получить список всех рейтингов
    @Override
    public List<Mpa> getMPAList() {
        String sql = "select MPA_id, MPA_name from rating_MPA ORDER BY MPA_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    //вернуть рейтинг по id
    @Override
    public Mpa getMPAById(int id) {
        SqlRowSet MPARows = jdbcTemplate.queryForRowSet("select * from rating_MPA where MPA_id = ?", id);
        if(MPARows.next()) {
            Mpa mpa = new Mpa(
                    MPARows.getInt("MPA_id"),
                    MPARows.getString("MPA_name")
            );
            log.info("Обработан запрос получения жанра {}", MPARows.getString("MPA_name"));
            return mpa;
        } else {
            return null;
        }
    }

    //получить объект МПА
    private static Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("MPA_id"),
                rs.getString("MPA_name")
        );
    }

    //поставить лайк
    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO like_list (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    //удалить лайк
    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM like_list where film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    //получить список пользователей, которые поставили фильму лайк
    @Override
    public List<Long> getLikesByFilm(long filmId) {
        String sql = "SELECT user_id FROM like_list WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), filmId);
    }

    //получить ИД лайка
    private static long makeLike(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }
}
