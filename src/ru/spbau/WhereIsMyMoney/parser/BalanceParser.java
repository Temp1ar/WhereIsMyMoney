package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

import java.io.Serializable;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 11:27 AM
 */
public class BalanceParser implements Parser {
    private String ignoreSymbols;
    private char decimalDelimiter;

    public BalanceParser(String ignoreSymbols, char decimalDelimiter) {
        this.ignoreSymbols = ignoreSymbols;
        this.decimalDelimiter = decimalDelimiter;
    }

    public boolean parse(String string, Transaction result) {
        String filteredString = new String(string);
        for (char c : ignoreSymbols.toCharArray()) {
            filteredString = filteredString.replaceAll(Character.toString(c), "");
        }
        filteredString = filteredString.replace(decimalDelimiter,'.');
        try {
            result.setBalance(new Float(filteredString));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getDescription() {
        return this.toString();
    }
}
