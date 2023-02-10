package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.AbsenceException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    HashMap<Long, User> getUserList();

    User getUserById(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    ArrayList<User> getFriendList(long id);

    ArrayList<User> getCommonFriends(long firstId, long secondId);
}
