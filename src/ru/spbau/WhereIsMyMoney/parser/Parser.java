package ru.spbau.WhereIsMyMoney.parser;
/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/12/12
 * Time: 3:19 PM
 */

import ru.spbau.WhereIsMyMoney.Transaction;

import java.io.Serializable;

public interface Parser extends Serializable {
    public boolean parse(String string, Transaction result);
    public String getRE();
    public String getDescription();
}
