package ru.spbau.WhereIsMyMoney;

import java.util.Date;
import java.util.regex.Pattern;

public class SmsEvent {
    private final String source;
    private final String body;
    private final Date date;
    private static final String DELIMITER = "\0";

    public SmsEvent(String source, String body, long date) {
        this.source = source;
        this.body = body;
        this.date = new Date();
        this.date.setTime(date);
    }

    public static SmsEvent parse(String message) {
        String[] parts = Pattern.compile(DELIMITER, Pattern.LITERAL).split(message);
        return new SmsEvent(parts[0], parts[1], Long.parseLong(parts[2]));
    }

    public String getSource() {
        return source;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return source + DELIMITER + body + DELIMITER + date.getTime();
    }
}
