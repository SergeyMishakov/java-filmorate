package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    //@NotNull
    private Mpa mpa;
    private int rate;
    //@JsonIgnore
    private List<Genre> genres;
    private final Set<Long> likeList = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate,
                int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = new ArrayList<Genre>();
    }

    @JsonCreator
    public Film(long id, String name, String description, LocalDate releaseDate,
                int duration, int rate, Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = loadGenres(genres);
    }

    //обработка массива жанров
    public List<Genre> loadGenres(List<Genre> genres) {
        if (genres == null) {
            return new ArrayList<>();
        } else {
            return genres;
        }
    }

    public void addLike(long userId) {
        likeList.add(userId);
    }

    public void deleteLike(long userId) {likeList.remove(userId); }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Genre genre) {
        genres.add(genre);
    }
}

