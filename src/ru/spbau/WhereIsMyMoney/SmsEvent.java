package ru.spbau.WhereIsMyMoney;

import java.util.Date;

public class SmsEvent {
    private final String source;
    private final String body;
    private final Date date;
    private static final char DELIMITER = 0;

    public SmsEvent(String source, String body, long date) {
        this.source = source;
        this.body = body;
        this.date = new Date();
        this.date.setTime(date);
    }

    public static SmsEvent parse(String message) {
        int delimiterPosition = message.indexOf(DELIMITER);
        int delimiterPosition2 = message.indexOf(DELIMITER, delimiterPosition + 1);
        return new SmsEvent(
            message.substring(0, delimiterPosition),
            message.substring(delimiterPosition + 1, delimiterPosition2),
            Long.parseLong(message.substring(delimiterPosition2 + 1, message.length()))
        );
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
