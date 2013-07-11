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

    public static Pair<Float, String> parse(String str) {
        Matcher matcher = CURRENCY_PATTERN.matcher(str);

        if (!matcher.matches()) return null;

        String amount = matcher.group(AMOUNT_GROUP).replaceAll("\\s", "");
        String currency = matcher.group(CURRENCY_GROUP);
        if (currency == null)
            currency = "";

        Float result = null;
        Character separatorCandidate = getSeparatorCandidate(amount);
        if (separatorCandidate != null) {
            result = parse(amount, separatorCandidate);
        } else {
            for (char sep : SEPARATORS) {
                result = parse(amount, sep);
                if (result != null) break;
            }
        }

        if (result == null) {
            Log.e(TAG, "Can't parse float from message: " + str);
        }

        return Pair.create(result, currency);
    }

    private static Character getSeparatorCandidate(String str) {
        if (str.length() > 2) {
            char sep = str.charAt(str.length() - 2);
            if (!Character.isDigit(sep)) return sep;
        }

        if (str.length() > 3) {
            char sep = str.charAt(str.length() - 3);
            if (!Character.isDigit(sep)) return sep;
        }

        return null;
    }

    private static Float parse(String amount, char separator) {
        amount = amount.replaceAll("[^\\d" + separator + "]", "").replace(separator, SEPARATOR);

        try {
            return Float.parseFloat(amount);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return null;
    }
}
