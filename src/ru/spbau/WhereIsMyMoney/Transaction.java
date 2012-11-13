package ru.spbau.WhereIsMyMoney;

import java.util.Date;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/12/12
 * Time: 3:32 PM
 */
public class Transaction {
	public static final int WITHDRAW = 0;
	public static final int DEPOSIT = 1;
	public static final int NONE = 2;
	
    private Date date;
    private String place;
    private String card;
    private String delta;
    private float balance;
    private int type;

    public Transaction() {
    }
    
    public Transaction(Date date, String place, String card, String delta, float balance, int type) {
        this.date = date;
        this.place = place;
        this.card = card;
        this.delta = delta;
        this.balance = balance;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getCard() {
        return card;
    }

    public String getDelta() {
        return delta;
    }

    public float getBalance() {
        return balance;
    }
    
    public int getType() {
    	return type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setType(int type) {
        this.type = type;
    }
}
