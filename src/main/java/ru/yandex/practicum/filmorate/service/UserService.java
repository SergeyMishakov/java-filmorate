package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private final UserStorage userStorage;


    //создать пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    //обновить пользователя
    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            try {
                throw new AbsenceException("Нельзя обновить несуществующего пользователя");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        return userStorage.updateUser(user);
    }

    //вернуть список пользователей
    public ArrayList<User> getUserList() {
        ArrayList<User> userList = new ArrayList<>(userStorage.getUserList().values());
        return userList;
    }

    //вернуть пользователя по идентификатору
    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            try {
                throw new AbsenceException("Такого пользователя не найдено");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    //добавить в друзья
    public void addFriend(long userId, long friendId) {
        if (userStorage.getUserById(friendId) == null) {
            try {
                throw new AbsenceException("Такого пользователя не существует");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
        userStorage.addFriend(userId, friendId);
    }

    //удалить из друзей
    public void deleteFriend(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    //вернуть список друзей пользователя
    public ArrayList<User> getFriendList(long id) {
        return userStorage.getFriendList(id);
    }

    //вернуть количество общих друзей
    public ArrayList<User> getCommonFriends(long firstId, long secondId) {
        return userStorage.getCommonFriends(firstId, secondId);
    }
}
