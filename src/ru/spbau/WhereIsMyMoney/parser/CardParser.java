package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 4:33 PM
 */
public class CardParser implements Parser {
    public boolean parse(String string, Transaction result) {
        result.setCard(string.trim());
        return true;
    }

}
