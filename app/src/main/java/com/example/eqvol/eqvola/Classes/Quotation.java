package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 30.01.2018.
 */

public class Quotation {

    private String Symbol;
    private double Bid;
    private double Ask;
    private String Direction;
    private double Spread;
    private int Digits;



    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public double getBid() {
        return Bid;
    }

    public void setBid(double bid) {
        Bid = bid;
    }

    public double getAsk() {
        return Ask;
    }

    public void setAsk(double ask) {
        Ask = ask;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public double getSpread() {
        return Spread;
    }

    public void setSpread(double spread) {
        Spread = spread;
    }

    public int getDigits() {
        return Digits;
    }

    public void setDigits(int digits) {
        this.Digits = digits;
    }
}
