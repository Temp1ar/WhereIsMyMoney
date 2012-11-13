package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/13/12
 * Time: 11:29 AM
 */
public class DateParser implements Parser, Serializable {
    DateFormat dateFormat;

    public DateParser(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean parse(String string, Transaction result) {
        try {
            Date date = dateFormat.parse(string);
            result.setDate(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public String getDescription() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "DateParser{" +
                "dateFormat=" + dateFormat +
                '}';
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
