package ru.spbau.WhereIsMyMoney.parser;

import android.util.Log;
import ru.spbau.WhereIsMyMoney.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Temp1ar
 * Date: 13.11.12
 * Time: 17:46
 */
public class BaltBankHelper {
    ArrayList<REParser> parsers = new ArrayList<REParser>();

    public BaltBankHelper() {
        Map<Integer, Parser> withPlace = new HashMap<Integer, Parser>();
        withPlace.put(1, new DateParser(new SimpleDateFormat("d-M-y k:m")));
        withPlace.put(2, new CardParser());
        withPlace.put(3, new DeltaParser());
        withPlace.put(4, new PlaceParser());
        withPlace.put(5, new BalanceParser("", '.'));

        Map<Integer, Parser> withoutPlace = new HashMap<Integer, Parser>();
        withoutPlace.put(1, new DateParser(new SimpleDateFormat("d-M-y k:m")));
        withoutPlace.put(2, new CardParser());
        withoutPlace.put(3, new DeltaParser());
        withoutPlace.put(4, new BalanceParser("", '.'));

        String bankCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+средств\\s+со\\s+счета\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String bankCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+sredstv\\s+so\\s+scheta\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        parsers.add(new REParser(withPlace, bankCashout, Transaction.WITHDRAW));
        parsers.add(new REParser(withPlace, bankCashoutEn, Transaction.WITHDRAW));

        String atmCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Получение\\s+наличных\\s+в\\s+банкомате\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String atmCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Poluchenie\\s+nalichnykh\\s+v\\s+bankomate\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        parsers.add(new REParser(withPlace, atmCashout, Transaction.WITHDRAW));
        parsers.add(new REParser(withPlace, atmCashoutEn, Transaction.WITHDRAW));

        String salaryDeposit = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Поступление\\s+зарплаты\\s+([^\\s]*\\s+[^;]*);Доступно:([^ ]*)";
        String salaryDepositEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Postuplenie\\s+zarplaty\\s+([^\\s]*\\s+[^;]*);Dostupno:([^ ]*)";
        parsers.add(new REParser(withoutPlace, salaryDeposit, Transaction.DEPOSIT));
        parsers.add(new REParser(withoutPlace, salaryDepositEn, Transaction.DEPOSIT));

        String smsComission = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+комиссии\\s+за\\s+пользование\\s+услугой\\s+SMS-Информирование\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+[^;]*?;Доступно:([^ ]*)";
        String smsComissionEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+komissii\\s+za\\s+pol'zovanie\\s+uslugoi\\s+SMS-Informirovanie\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+[^;]*?;Dostupno:([^ ]*)";
        parsers.add(new REParser(withoutPlace, smsComission, Transaction.WITHDRAW));
        parsers.add(new REParser(withoutPlace, smsComissionEn, Transaction.WITHDRAW));

        String goodsWithdraw = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Оплата\\s+товаров/услуг\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String goodsWithdrawEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Oplata\\s+tovarov/uslug\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        parsers.add(new REParser(withPlace, goodsWithdraw, Transaction.WITHDRAW));
        parsers.add(new REParser(withPlace, goodsWithdrawEn, Transaction.WITHDRAW));
    }

    public Transaction tryParse(String message) {
        Transaction transaction = new Transaction();
        for (int i = 0; i < parsers.size(); ++i) {
                if (parsers.get(i).parse(message, transaction)) {
                    return transaction;
                }
        }
        return null;
    }
}
