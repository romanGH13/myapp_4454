package com.example.eqvol.eqvola.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqvol on 25.10.2017.
 */

public class Group {
    private int id;
    private String name;
    private String code;
    private String date;
    private List<Leverage> leverages; // работает для получения групп в создании счета
    //private String leverages; //для просмотра всех счетов
    private int parent_group_id;
    private int is_cent;
    private int is_micro;
    private int is_active;


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

    public List<String> getLeverages() {
        List<String> leverages = new ArrayList<>();

        for(Leverage l: this.leverages)
        {
            leverages.add(l.getLeverage());
        }

        //my account
        /*String arrayLeverages[] = this.leverages.split(", ");
        for(String l: arrayLeverages)
        {
            leverages.add(l.trim());
        }*/

        return leverages;
    }

    /*public void setLeverages(String leverages) {
        this.leverages = leverages;
    }*/
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
