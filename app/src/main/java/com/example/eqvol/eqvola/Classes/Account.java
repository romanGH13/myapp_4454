package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 26.10.2017.
 */

public class Account {
    private int Login;
    private int MqId;
    private String Email;
    private String Phone;
    private Group Group;
    private String Name;
    private String Country;
    private String City;
    private String State;
    private String ZipCode;
    private String Address;
    private String UserColor;
    private String Enable;
    private String EnableChengePassword;
    private String EnableReadOnly;
    private String EnableOTP;
    private String PasswordPhone;
    private String LeadSource;
    private String Comment;
    private String Status;
    private int Leverage;
    private String AgentAccount;
    private String Balance;
    private String PrevMonthBalance;
    private String PrevBalance;
    private String Credit;
    private String InterestRate;
    private String Taxes;
    private String SendReports;
    private String PrevEquity;
    private String PrevMonthEquity;
    private String RegisterDate;
    private String LastDate;
    private String Timestamp;

    private boolean isDetailOpen = false;

    public int getLogin() {
        return Login;
    }

    public void setLogin(int login) {
        Login = login;
    }

    public com.example.eqvol.eqvola.Classes.Group getGroup() {
        return Group;
    }

    public void setGroup(com.example.eqvol.eqvola.Classes.Group group) {
        Group = group;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }

    public int getLeverage() {
        return Leverage;
    }

    public void setLeverage(int leverage) {
        Leverage = leverage;
    }

    public int getMqId() {
        return MqId;
    }

    public void setMqId(int mqId) {
        MqId = mqId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



    public String getPasswordPhone() {
        return PasswordPhone;
    }

    public void setPasswordPhone(String passwordPhone) {
        PasswordPhone = passwordPhone;
    }

    public String getLeadSource() {
        return LeadSource;
    }

    public void setLeadSource(String leadSource) {
        LeadSource = leadSource;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public boolean isDetailOpen() {
        return isDetailOpen;
    }

    public void setDetailOpen(boolean detailOpen) {
        isDetailOpen = detailOpen;
    }
}
