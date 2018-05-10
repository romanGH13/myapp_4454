package com.example.eqvol.eqvola.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqvol on 13.10.2017.
 */


public class User{
    private int id;
    private String email;
    private String date;
    private int rights;
    private int is_approved;
    private String language;
    private String name;
    private String birth_date;
    private int phone_code;
    private String phone_number;
    private String country;
    private String city;
    private String state;
    private String street;
    private String postal_code;

    transient private byte[] avatar;
    public List<Ticket> tickets;
    public List<Account> accounts;
    private int currentTicketId;

    public User(){}


    public User(int id, String name, String email, String birth_date, String country,
                String state, String city, String street, String postal_code, int phone_code,
                String phone_number, String date, String language, int is_approved, int rights)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.postal_code = postal_code;
        this.phone_code = phone_code;
        this.phone_number = phone_number;
        this.date = date;
        this.language = language;
        this.is_approved = is_approved;
        this.rights = rights;
    }

    public void updateMetaData(User user)
    {
        this.name = user.name;
        this.birth_date = user.birth_date;
        this.country = user.country;
        this.state = user.state;
        this.city = user.city;
        this.street = user.street;
        this.postal_code = user.postal_code;
        this.phone_code = user.phone_code;
        this.phone_number = user.phone_number;
    }

    public void addTicket(Ticket ticket)
    {
        if(this.tickets == null){
            tickets = new ArrayList<Ticket>();
        }
        this.tickets.add(ticket);
        for(int i = 0; i < this.tickets.size(); i++) {
            if(this.tickets.get(i).equals(ticket)) {
                this.currentTicketId = i;
            }
        }
    }

    public void setTicket(Ticket ticket)
    {
        this.tickets.add(ticket);
    }
    public Ticket getTicket(int id)
    {
        return this.tickets.get(id);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public int getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(int phone_code) {
        this.phone_code = phone_code;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public int getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(int is_approved) {
        this.is_approved = is_approved;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getCurrentTicketId() {
        return currentTicketId;
    }

    public void setCurrentTicketId(int currentTicketId) {
        this.currentTicketId = currentTicketId;
    }

}

