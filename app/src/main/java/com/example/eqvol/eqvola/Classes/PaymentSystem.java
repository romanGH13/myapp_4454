package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 26.02.2018.
 */

public class PaymentSystem {
    private int id;
    private String code;
    private String title;
    private String description;
    private String currencies;
    private String commission;
    private String deposit_time;
    private String withdrawal_time;
    private String request_data;
    private String is_withdrawal;
    private String is_deposit;
    private String date;

    transient private byte[] avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDeposit_time() {
        return deposit_time;
    }

    public void setDeposit_time(String deposit_time) {
        this.deposit_time = deposit_time;
    }

    public String getWithdrawal_time() {
        return withdrawal_time;
    }

    public void setWithdrawal_time(String withdrawal_time) {
        this.withdrawal_time = withdrawal_time;
    }

    public String getRequest_data() {
        return request_data;
    }

    public void setRequest_data(String request_data) {
        this.request_data = request_data;
    }

    public String getIs_withdrawal() {
        return is_withdrawal;
    }

    public void setIs_withdrawal(String is_withdrawal) {
        this.is_withdrawal = is_withdrawal;
    }

    public String getIs_deposit() {
        return is_deposit;
    }

    public void setIs_deposit(String is_deposit) {
        this.is_deposit = is_deposit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
