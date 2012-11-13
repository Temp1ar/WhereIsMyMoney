package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Alexander Opeykin alexander.opeykin@gmail.com
 * Date: 11/12/12
 * Time: 3:25 PM
 */
public class REParser implements Parser {
    private Pattern pattern;
    private Map<Integer, Parser> parsers;
    private String re;


    public REParser(Map<Integer, Parser> parsers, String re) {
        this.parsers = parsers;
        this.re = re;
        pattern = Pattern.compile(re);
    }

    public boolean parse(String string, Transaction result) {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.find()) {
            return false;
        }
        for (Map.Entry<Integer, Parser> entry : parsers.entrySet()) {
            Parser parser = entry.getValue();
            Integer groupN = entry.getKey();
            String subString = matcher.group(groupN);
            if (!parser.parse(subString, result)) {
                return false;
            }
        }
        return true;
    }

    public String getDescription() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "REParser{" +
                "re='" + re + '\'' +
                '}';
    }
}
