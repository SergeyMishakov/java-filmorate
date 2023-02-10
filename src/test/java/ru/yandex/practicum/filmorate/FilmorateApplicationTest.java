package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    //ТЕСТЫ ДЛЯ USER
//    User createUser(User user);
    @Test
    @Order(1)
    public void testCreateUser() {
        User user = new User(1,
                "testLogin",
                "testName",
                "testEmail@mail.ru",
                LocalDate.of(1991, 04,21));
        User resultUser = userStorage.createUser(user);
        assertThat(resultUser)
                .isNotNull()
                .isInstanceOf(User.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "testLogin")
                .hasFieldOrPropertyWithValue("name", "testName")
                .hasFieldOrPropertyWithValue("email", "testEmail@mail.ru")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1991, 04,21));
    }

//    User updateUser(User user);
    @Test
    @Order(2)
    public void testUpdateUser() {
        User userForUpdate = new User(1,
                "updateTestLogin",
                "testName",
                "testEmail@mail.ru",
                LocalDate.of(1991, 04,21));
        User resultUser = userStorage.updateUser(userForUpdate);
        assertThat(resultUser)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "updateTestLogin");
    }

//    HashMap<Integer, User> getUserList();
    @Test
    @Order(3)
    public void testGetUserList() {
    HashMap<Long, User> userList = userStorage.getUserList();
        assertThat(userList)
                .isNotNull();
        assertThat(userList.size()).isEqualTo(1);
    }

//    User getUserById(int id);
    @Test
    @Order(4)
    public void testGetUserById() {
        User user = new User(1,
                "updateTestLogin",
                "testName",
                "testEmail@mail.ru",
                LocalDate.of(1991, 04, 21));
        User resultUser = userStorage.getUserById(1);
        assertThat(resultUser).isEqualTo(user);
    }
//
//    void addFriend(int userId, int friendId);
    @Test
    @Order(5)
    public void testAddFriendById() {
        User user = new User(2,
                "friend",
                "friendName",
                "friendEmail@mail.ru",
                LocalDate.of(1993, 04, 21));
        userStorage.createUser(user);
        userStorage.addFriend(1,2);
        assertThat(userStorage.getFriendList(1)).isInstanceOf(ArrayList.class);
    }

//    void deleteFriend(int userId, int friendId);
    @Test
    @Order(6)
    public void testDeleteFriend() {
        userStorage.deleteFriend(1,2);
        assertThat(userStorage.getFriendList(1).size()).isEqualTo(0);
    }

//    ArrayList<User> getCommonFriends(int firstId, int secondId);
    @Test
    @Order(7)
    public void testGetCommonFriends() {
        User user = new User(3,
                "friend2",
                "friendName",
                "friendEmail@mail.ru",
                LocalDate.of(1993, 04, 21));
        userStorage.createUser(user);
        userStorage.addFriend(1,3);
        userStorage.addFriend(2,3);
        ArrayList<User> commonFriend = userStorage.getCommonFriends(1, 2);
        assertThat(commonFriend.size()).isEqualTo(1);
        assertThat(commonFriend.get(0)).isEqualTo(user);
    }

//ТЕСТЫ ДЛЯ FILM

    //    Film addFilm(Film film);

    @Test
    @Order(8)
    public void testAddFilm() {
        Film film = new Film(
                1,
                "Film",
                "Description of film",
                LocalDate.of(2021, 12, 31),
                180,
                new Mpa(1),
                1,
                new ArrayList<>(1)
        );
        Film resultFilm = filmStorage.addFilm(film);
        assertThat(resultFilm)
                .isNotNull()
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Film");
    }
//    Film updateFilm(Film film);
    @Test
    @Order(9)
    public void testUpdateFilm() {
        Film film = new Film(
                1,
                "updatedFilm",
                "Description of film",
                LocalDate.of(2021, 12, 31),
                180,
                new Mpa(1),
                1,
                new ArrayList<>(1)
        );
        Film resultFilm = filmStorage.updateFilm(film);
        assertThat(resultFilm)
                .isNotNull()
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "updatedFilm");
    }

//    HashMap<Integer, Film> getFilmList();
    @Test
    @Order(10)
    public void testGetFilmList() {
        HashMap<Long, Film> filmList = filmStorage.getFilmList();
        assertThat(filmList.size()).isEqualTo(1);
        assertThat(filmList.get(1L))
                .isNotNull()
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "updatedFilm");
    }

//    Film getFilmById(int id);
    @Test
    @Order(11)
    public void testGetFilmById() {
        Film film = filmStorage.getFilmById(1);
        assertThat(film)
                .isNotNull()
                .isInstanceOf(Film.class)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "updatedFilm");
    }
//
//    List<Genre> getGenreList();
    @Test
    @Order(12)
    public void testGetGenreList() {
        List<Genre> genreList = filmStorage.getGenreList();
        assertThat(genreList.size()).isEqualTo(6);
        assertThat(genreList.get(0)).hasFieldOrPropertyWithValue("id", 1)
                                    .hasFieldOrPropertyWithValue("name", "Комедия");
    }
//
//    Genre getGenreById(int id);
    @Test
    @Order(13)
    public void testGetGenreById() {
        Genre genre = filmStorage.getGenreById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                        .hasFieldOrPropertyWithValue("name", "Комедия");
    }
//
//    List<Mpa> getMPAList();
    @Test
    @Order(14)
    public void testGetMPAList() {
        List<Mpa> MpaList = filmStorage.getMPAList();
        assertThat(MpaList.size()).isEqualTo(5);
        assertThat(MpaList.get(0)).hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }
//
//    Mpa getMPAById(int id);
    @Test
    @Order(15)
    public void testGetMPAById() {
        Mpa mpa = filmStorage.getMPAById(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }
//
//    void addLike(int filmId, long userId);
    @Test
    @Order(16)
    public void testAddLike() {
        filmStorage.addLike(1,1);
        assertThat(filmStorage.getLikesByFilm(1).size()).isEqualTo(1);
    }
//
//    void deleteLike(int filmId, long userId);
    @Test
    @Order(17)
    public void testDeleteLike() {
        filmStorage.deleteLike(1,1);
        assertThat(filmStorage.getLikesByFilm(1).size()).isEqualTo(0);
    }
}