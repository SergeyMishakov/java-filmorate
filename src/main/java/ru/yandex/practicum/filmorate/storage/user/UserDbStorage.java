package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component("userDbStorage")

public class UserDbStorage implements UserStorage{

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //создать нового пользователя
    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users(login, name, email, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        return getUserById(id);
    };

    //обновить данные пользователя
    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "login = ?, name = ?, email = ?, birthday = ?" + "where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getLogin()
                , user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        return getUserById(user.getId());
    };

    //получить список пользователей
    @Override
    public HashMap<Long, User> getUserList() {
        String sql = "SELECT * FROM USERS";
        HashMap<Long, User> userList = new HashMap<>();
        List<User> userArrayList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        for (User user: userArrayList) {
            userList.put(user.getId(), user);
        }
        return userList;
    };

    //получить пользователя по id
    @Override
    public User getUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate()
            );
            log.info("Обработан запрос получения пользователя {}", userRows.getString("login"));
            return user;
        } else {
            log.info("Пользователь не найден");
            return null;
        }
    };

    //получить объект User
    private static User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate()
        );
    }

    //добавить в друзья
    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "INSERT INTO friend_list (user_id, friend_id) " +
                "values (?, ?)";
         jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    //удалить из друзей
    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friend_list where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    //вернуть список друзей
    @Override
    public ArrayList<User> getFriendList(long id) {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT fl.friend_id AS user_id, " +
                        "u.email AS email, " +
                        "u.login AS login, " +
                        "u.name AS name, " +
                        "u.birthday AS birthday " +
                "FROM friend_list AS fl JOIN users AS u ON fl.friend_id = u.user_id " +
                "WHERE fl.user_id = ?";
        List<User> userArrayList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        userList.addAll(userArrayList);
        return userList;
    }

    //получить список общих друзей
    public ArrayList<User> getCommonFriends(long firstId, long secondId) {
        ArrayList<User> commonFriendsList = new ArrayList<>();
        String sql = "SELECT u.user_id user_id, " +
                "             u.email email, " +
                "            u.login login, " +
                "            u.name name, " +
                "            u.birthday birthday, " +
                "            fl.friend_id " +
                "        FROM (SELECT friend_id, " +
                "            COUNT(user_id) " +
                "            FROM friend_list " +
                "            WHERE user_id IN (?, ?) " +
                "            GROUP BY friend_id " +
                "            HAVING COUNT(user_id) > 1) AS fl INNER JOIN users AS u ON fl.friend_id = u.user_id";
        List<User> userArrayList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), firstId, secondId);
        commonFriendsList.addAll(userArrayList);
        return commonFriendsList;
    }
}
