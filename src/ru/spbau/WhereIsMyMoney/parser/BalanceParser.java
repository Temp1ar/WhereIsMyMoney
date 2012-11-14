package ru.spbau.WhereIsMyMoney.parser;

import android.util.Log;
import ru.spbau.WhereIsMyMoney.Transaction;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 11:27 AM
 */
public class BalanceParser implements Parser {
    private final String ignoreSymbols;
    private final char decimalDelimiter;

    public BalanceParser(String ignoreSymbols, char decimalDelimiter) {
        this.ignoreSymbols = ignoreSymbols;
        this.decimalDelimiter = decimalDelimiter;
    }

    public boolean parse(String string, Transaction result) {
        String filteredString = string;
        for (char c : ignoreSymbols.toCharArray()) {
            filteredString = filteredString.replaceAll(Character.toString(c), "");
        }
        filteredString = filteredString.replace(decimalDelimiter,'.');
        try {
            result.setBalance(Float.valueOf(filteredString));
            return true;
        } catch (NumberFormatException e) {
            Log.e(this.getClass().getCanonicalName(), "Can't parse: " + string + " with " + this);
            return false;
        }
    }

}
