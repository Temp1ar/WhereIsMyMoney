package ru.spbau.WhereIsMyMoney;

public class Card {
    private final String id;
    private final Float balance;

    public Card(String id, Float balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public Float getBalance() {
        return balance;
    }
}
