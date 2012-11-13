package ru.spbau.WhereIsMyMoney;

import java.text.SimpleDateFormat;
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
    
    @Override
    public String toString() {
    	String FORMAT = "yyyy.MM.dd HH:mm:ss";
    	SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT);
    	StringBuilder sb = new StringBuilder();
    	sb.append(FORMATTER.format(getDate())).append("\n");
    	sb.append("Balance: ").append(getBalance()).append("\n");
    	sb.append("Card: ").append(getCard()).append("\n");
    	if (getDelta() != null) {
    		sb.append("Delta: ").append(getDelta()).append("\n");
    	}
    	if (getPlace() != null) {
    		sb.append("Place: ").append(getPlace()).append("\n");
    	}
    	return sb.toString();
    }
}
