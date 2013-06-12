package ru.spbau.WhereIsMyMoney2;

public class Card {
    private final String id;
    private final String displayName;
    private final Float balance;


    public Card(String id, Float balance) {
        this(id, id, balance);

    }

    public Card(String id, String displayName, Float balance) {
        this.id = id;
        this.displayName = displayName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Float getBalance() {
        return balance;
    }
}
