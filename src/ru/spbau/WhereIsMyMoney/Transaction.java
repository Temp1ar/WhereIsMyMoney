package ru.spbau.WhereIsMyMoney;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/12/12
 * Time: 3:32 PM
 */
public class Transaction {
	private static final String FORMAT = "yyyy.MM.dd HH:mm:ss";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT);
	
    private Date date;
    private String place;
    private String card;
    private String delta;
    private float balance;

    public Transaction(Date date, String place, String card, String delta, float balance) {
        this.date = date;
        this.place = place;
        this.card = card;
        this.delta = delta;
        this.balance = balance;
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
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(FORMATTER.format(getDate())).append("\n");
    	sb.append("Balance: ").append(getBalance()).append("\n");
    	if (getDelta() != null) {
    		sb.append("Delta: ").append(getDelta()).append("\n");
    	}
    	if (getPlace() != null) {
    		sb.append("Place: ").append(getPlace()).append("\n");
    	}
    	return sb.toString();
    }
}
