package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private final UserStorage userStorage;


    //создать пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    //обновить пользователя
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    //вернуть список пользователей
    public ArrayList<User> getUserList() {
        ArrayList<User> userList = new ArrayList<>(userStorage.getUserList().values());
        return userList;
    }

    //вернуть пользователя по идентификатору
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    //добавить в друзья
    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        userStorage.updateUser(user);
        friend.addFriend(userId);
        userStorage.updateUser(friend);
    }

    //удалить из друзей
    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        user.deleteFriend(friendId);
        userStorage.updateUser(user);
        User friend = userStorage.getUserById(friendId);
        friend.deleteFriend(userId);
        userStorage.updateUser(friend);
    }

    //вернуть список друзей пользователя
    public ArrayList<User> getFriendList(int id) {
        User user = userStorage.getUserById(id);
        ArrayList<User> friendList = new ArrayList<>();
        Set<Integer> friendIdList = user.getFriendList();
        for (Integer friendId: friendIdList) {
            friendList.add(userStorage.getUserById(friendId));
        }
        return friendList;
    }

    //вернуть количество общих друзей
    public ArrayList<User> getCommonFriends(int firstId, int secondId) {
        ArrayList<User> commonFriendList = new ArrayList<>();
        User firstUser = userStorage.getUserById(firstId);
        Set<Integer> firstFriendList = firstUser.getFriendList();
        User secondUser = userStorage.getUserById(secondId);
        Set<Integer> secondFriendList = secondUser.getFriendList();
        Set<Integer> resultFriendList = new HashSet<>(firstFriendList);
        resultFriendList.retainAll(secondFriendList);
        for (Integer id : resultFriendList) {
            commonFriendList.add(userStorage.getUserById(id));
        }
        return commonFriendList;
    }
}
