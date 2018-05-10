package com.example.eqvol.eqvola.Classes;

import java.util.List;

/**
 * Created by eqvol on 31.10.2017.
 */

public class Ticket{
    private int id;
    private String title;
    private int status;//0-open; 1-closed
    private String date;
    private User user;
    private Category category;
    private Message last_message;
    private int not_showed;

    private List<Message> messages;

    public Ticket()
    {

    }

    public Ticket(Category category, String title, Message message)
    {
        this.category = category;
        this.title = title;
        this.last_message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Message getMessage() {
        return this.last_message;
    }

    public void setMessage(Message message) {
        this.last_message = message;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNot_showed() {
        return not_showed;
    }

    public void setNot_showed(int not_showed) {
        this.not_showed = not_showed;
    }



}
