package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 11:27 AM
 */
public class MoneyParser implements Parser {
    private String ignoreSymbols;
    private char decimalDelimiter;

    public MoneyParser(String ignoreSymbols, char decimalDelimiter) {
        this.ignoreSymbols = ignoreSymbols;
        this.decimalDelimiter = decimalDelimiter;
    }

    public boolean parse(String string, Transaction result) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getRE() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getDescription() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "MoneyParser{" +
                "ignoreSymbols='" + ignoreSymbols + '\'' +
                ", decimalDelimiter=" + decimalDelimiter +
                '}';
    }
}
