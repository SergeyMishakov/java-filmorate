package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    @NotBlank
    private String login;
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Past
    private LocalDate birthday;

    @JsonIgnore
    private final Set<Integer> friendList = new HashSet<>();

    public void addFriend(int userId) {
        friendList.add(userId);
    }

    public void deleteFriend(int userId) {
        friendList.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(birthday, user.birthday) && Objects.equals(friendList, user.friendList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, name, email, birthday, friendList);
    }
}
