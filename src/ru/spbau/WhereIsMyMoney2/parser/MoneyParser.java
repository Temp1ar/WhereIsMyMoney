package ru.spbau.WhereIsMyMoney2.parser;


import android.util.Log;
import android.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class MoneyParser {
    private static final String TAG = MoneyParser.class.getCanonicalName();

    private static final char[] SEPARATORS = {'.', ','};
    private static final char SEPARATOR = '.';

    private static final int AMOUNT_GROUP = 1;
    private static final int CURRENCY_GROUP = 2;

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("([^a-zA-Z]*)(\\w*).*");

    static public Pair<Float, String> parse(String str) {
        for (char sep : SEPARATORS) {
            Pair<String, String> money = formatWithSeparator(str, sep);
            try {
                return new Pair<Float, String>(Float.parseFloat(money.first), money.second);
            } catch (Exception e) {
            }
        }
        Log.e(TAG, "Can't parse float from message: " + str);
        return null;
    }

    static private Pair<String, String> formatWithSeparator(String str, char sep) {
        Matcher matcher = CURRENCY_PATTERN.matcher(str);
        String amount = "";
        String currency = "";

        if (matcher.matches()) {
            amount = matcher.group(AMOUNT_GROUP);
            currency = matcher.group(CURRENCY_GROUP);
            if (currency == null)
                currency = "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount.length(); ++i) {
            char c = amount.charAt(i);
            if (c == sep) {
                c = SEPARATOR;
            }
            if (Character.isDigit(c) || (c == SEPARATOR)) {
                sb.append(c);
            }
        }
        return new Pair<String, String>(sb.toString(), currency);
    }
}
