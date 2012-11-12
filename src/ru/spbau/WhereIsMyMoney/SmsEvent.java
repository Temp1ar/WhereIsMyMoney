package ru.spbau.WhereIsMyMoney;

public class SmsEvent {
    private final String source;
    private final String body;
    private static final char DELIMITER = 0;

    public SmsEvent(String source, String body) {
        this.source = source;
        this.body = body;
    }

    public static SmsEvent parse(String message) {
        int delimiterPosition = message.indexOf(DELIMITER);
        return new SmsEvent(message.substring(0, delimiterPosition),
            message.substring(delimiterPosition + 1, message.length()));
    }

    @Override
    public String toString() {
        return source + DELIMITER + body;
    }
}
