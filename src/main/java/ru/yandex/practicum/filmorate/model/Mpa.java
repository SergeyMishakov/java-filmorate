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

    public int getId() {
        return id;
    }

    public void setId(int mpaId) {
        this.id = mpaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mpaName) {
        this.name = mpaName;
    }
}
