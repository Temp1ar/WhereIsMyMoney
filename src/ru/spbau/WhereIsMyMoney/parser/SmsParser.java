package ru.spbau.WhereIsMyMoney.parser;

import android.content.Context;
import android.util.Pair;
import ru.spbau.WhereIsMyMoney.SmsEvent;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TemplatesSource;

import java.util.List;
import java.util.Map;

public class SmsParser {
    private static final String CARD = "card";
    private static final String PLACE = "place";
    private static final String DELTA = "delta";
    private static final String BALANCE = "balance";

    private final List<Template> templates;

    public SmsParser(Context context) {
        this(getTemplatesFromDatabase(context));
    }

    public SmsParser(List<Template> templates) {
        this.templates = templates;
    }

    private static List<Template> getTemplatesFromDatabase(Context context) {
        TemplatesSource db = new TemplatesSource(context);
        db.open();
        List<Template> templates = db.getTemplates();
        db.close();
        return templates;
    }

    public Transaction parseSms(SmsEvent sms) {
        String body = sms.getBody();
        for (Template template : templates) {
            TemplateMatcher matcher = new TemplateMatcher(template.getTemplate());

            Map<String, String> args = matcher.match(body);
            if (args != null) {
                Transaction transaction = makeTransaction(sms, args, template.getType());
                if (transaction != null) {
                    return transaction;
                }
            }
        }

        return null;
    }

    private Transaction makeTransaction(SmsEvent sms, Map<String, String> args, int type) {
        String card = args.get(CARD);
        String place = args.get(PLACE);
        String delta = args.get(DELTA);
        String balanceStr = args.get(BALANCE);
        if (balanceStr == null) {
            return null;
        }

        Pair<Float, String> money = MoneyParser.parse(balanceStr);
        Float balance = money.first;
        if (balance != null) {
            return new Transaction(sms.getDate(), place, card, delta, balance, type);
        }
        return null;
    }
}
