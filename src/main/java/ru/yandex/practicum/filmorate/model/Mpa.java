package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {

    public Mpa(int id, String mpaName) {
        this.id = id;
        this.name = mpaName;
    }

    public Mpa(int id) {
        this.id = id;
    }

    private int id;
    private String name;
}
