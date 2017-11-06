package com.example.eqvol.eqvola.Classes;

import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class Group {
    private int id;
    private String name;
    private String code;
    private String date;
    private List<Leverage> leverages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Leverage> getLeverages() {
        return leverages;
    }

    public void setLeverages(List<Leverage> leverages) {
        this.leverages = leverages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
