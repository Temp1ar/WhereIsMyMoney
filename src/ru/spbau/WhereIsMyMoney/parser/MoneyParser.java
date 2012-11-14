package ru.spbau.WhereIsMyMoney.parser;


import android.util.Log;

class MoneyParser {
    private static final char[] SEPARATORS = {'.', ','};
    private static final char SEPARATOR = '.';

    public Float parse(String str) {
        for (char sep : SEPARATORS) {
            String num = formatWithSeparator(str, sep);
            try {
                return Float.parseFloat(num);
            } catch (Exception e) {
            }
        }
        Log.e(getClass().getCanonicalName(), "Can't parse float from message: " + str);
        return null;
    }

    private String formatWithSeparator(String str, char sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c == sep) {
                c = SEPARATOR;
            }
            if (Character.isDigit(c) || (c == SEPARATOR)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
