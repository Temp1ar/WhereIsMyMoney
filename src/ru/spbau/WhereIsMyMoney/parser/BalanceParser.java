package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 11:27 AM
 */
public class BalanceParser implements Parser {
    private String ignoreSymbols;
    private char decimalDelimiter;
    float koef;

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
        result.setBalance(new Float(filteredString));
        return true;
    }

    public String getRE() {
        return "[\\d" + ignoreSymbols + decimalDelimiter + "]*";
    }

    public String getDescription() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "BalanceParser{" +
                "ignoreSymbols='" + ignoreSymbols + '\'' +
                ", decimalDelimiter=" + decimalDelimiter +
                '}';
    }

    public String getIgnoreSymbols() {
        return ignoreSymbols;
    }

    public char getDecimalDelimiter() {
        return decimalDelimiter;
    }

    public float getKoef() {
        return koef;
    }

    public void setIgnoreSymbols(String ignoreSymbols) {
        this.ignoreSymbols = ignoreSymbols;
    }

    public void setDecimalDelimiter(char decimalDelimiter) {
        this.decimalDelimiter = decimalDelimiter;
    }

    public void setKoef(float koef) {
        this.koef = koef;
    }
}
