package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.utils.StringUtils;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 4:36 PM
 */
public class CapslockedPlaceParser implements Parser {
    public boolean parse(String string, Transaction result) {
        result.setPlace(StringUtils.capitalizeString(string.trim().toLowerCase()));
        return true;
    }

    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
