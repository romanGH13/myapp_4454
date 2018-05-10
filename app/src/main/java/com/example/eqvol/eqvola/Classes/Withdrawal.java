package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 29.12.2017.
 */

public class Withdrawal {
    private int id;
    private int login;
    private double amount;
    private double commission;
    private String wallet;
    private String transaction;
    private String comment;
    private int status;
    private String date;
    private String date_success;
    private PaymentSetting payment_setting;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PaymentSetting getPayment_setting() {
        return payment_setting;
    }

    public void setPayment_setting(PaymentSetting payment_setting) {
        this.payment_setting = payment_setting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate_success() {
        return date_success;
    }

    public void setDate_success(String date_success) {
        this.date_success = date_success;
    }
}
