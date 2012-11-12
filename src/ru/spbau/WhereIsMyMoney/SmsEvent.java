package ru.spbau.WhereIsMyMoney;

public class SmsEvent {
    private final String originatingAddress;
    private final String displayMessageBody;
    private static final char DELIMITER = 0;

    public SmsEvent(String originatingAddress, String displayMessageBody) {
        this.originatingAddress = originatingAddress;
        this.displayMessageBody = displayMessageBody;
    }

    public SmsEvent(String representation) {
        int delimiterPosition = representation.indexOf(DELIMITER);
        originatingAddress = representation.substring(0, delimiterPosition);
        displayMessageBody = representation.substring(delimiterPosition + 1, representation.length());
    }

    @Override
    public String toString() {
        return originatingAddress + DELIMITER + displayMessageBody;
    }
}
