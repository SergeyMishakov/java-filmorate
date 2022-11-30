package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;

    private final Set<Integer> friendList = new HashSet<>();

    public void addFriend(int userId) {
        friendList.add(userId);
    }

    public void deleteFriend(int userId) {
        friendList.remove(userId);
    }
}
