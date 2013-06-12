package ru.spbau.WhereIsMyMoney2;

import android.util.Pair;
import ru.spbau.WhereIsMyMoney2.parser.MoneyParser;

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
    private String currency = null;
    private float amount = 0;

    public Transaction(Date date, String place, String card, String delta, float balance, int type) {
        this.date = date;
        this.place = place == null ? "" : place;
        this.card = card;
        this.delta = delta;
        this.balance = balance;
        this.type = type;

        Pair<Float, String> money = MoneyParser.parse(delta);
        this.amount = money.first;
        this.currency = money.second;
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

    public float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public float getBalance() {
        return balance;
    }

    public int getType() {
        return type;
    }
}
