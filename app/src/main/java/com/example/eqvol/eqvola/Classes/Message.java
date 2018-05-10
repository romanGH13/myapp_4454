package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 31.10.2017.
 */

public class Message{
    private int id;
    private String message;
    private int is_ready;
    private int is_showed;
    private String date;
    private User user;
    private Ticket ticket;
    private Attachment attachment;

    public Message(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIs_ready() {
        return is_ready;
    }

    public void setIs_ready(int is_ready) {
        this.is_ready = is_ready;
    }

    public int getIs_showed() {
        return is_showed;
    }

    public void setIs_showed(int is_showed) {
        this.is_showed = is_showed;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
