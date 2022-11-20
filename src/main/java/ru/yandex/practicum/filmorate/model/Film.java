package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.AbsenceException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;

    private final Set<Long> likeList = new HashSet<>();;

    public void addLike(Long userId) {
        likeList.add(userId);
    }

    public void deleteLike(Long userId) {
        if (likeList.contains(userId)) {
            likeList.remove(userId);
        } else {
            try {
                throw new AbsenceException("Данный пользователь не ставил лайк этому фильму");
            } catch (AbsenceException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
