package com.example.eqvol.eqvola.Classes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eqvol on 31.10.2017.
 */

public class Message{
    private int id;
    private int ticket_id;
    private String message;
    private int is_ready;
    private int is_showed;
    private String date;
    private User user;
    private Ticket ticket;
    private List<Object> attachment;

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

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    /*public boolean isIs_showed() {
        return is_showed;
    }

    public void setIs_showed(boolean is_showed) {
        this.is_showed = is_showed;
    }
*/
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

    public List<Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Object> attachment) {
        this.attachment = attachment;
    }

    /*public boolean isIs_ready() {
        return is_ready;
    }

    public void setIs_ready(boolean is_ready) {
        this.is_ready = is_ready;
    }*/
}
