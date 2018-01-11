package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 29.12.2017.
 */

public class PaymentSetting {
    private int id;
    private String code;
    private String title;
    private String description;
    private String currencies;
    private String commission;
    private String deposit_time;
    private String withdrawal_time;
    private String request_data;
    private int is_deposit;
    private int is_withdrawal;
    private String date;

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

    public String getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
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

    public int isIs_deposit() {
        return is_deposit;
    }

    public void setIs_deposit(int is_deposit) {
        this.is_deposit = is_deposit;
    }

    public int isIs_withdrawal() {
        return is_withdrawal;
    }

    public void setIs_withdrawal(int is_withdrawal) {
        this.is_withdrawal = is_withdrawal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
