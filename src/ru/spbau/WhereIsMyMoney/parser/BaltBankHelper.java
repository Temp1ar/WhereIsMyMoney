package ru.spbau.WhereIsMyMoney.parser;

import ru.spbau.WhereIsMyMoney.Transaction;

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
        String bankCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+средств\\s+со\\s+счета\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String bankCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+sredstv\\s+so\\s+scheta\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        String atmCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Получение\\s+наличных\\s+в\\s+банкомате\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String atmCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Poluchenie\\s+nalichnykh\\s+v\\s+bankomate\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        String salaryDeposit = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Поступление\\s+зарплаты\\s+([^\\s]*\\s+[^;]*);Доступно:([^ ]*)";
        String salaryDepositEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Postuplenie\\s+zarplaty\\s+([^\\s]*\\s+[^;]*);Dostupno:([^ ]*)";
        String goodsWithdraw = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Оплата\\s+товаров/услуг\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String goodsWithdrawEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Oplata\\s+tovarov/uslug\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";
        String smsComission = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+комиссии\\s+за\\s+пользование\\s+услугой\\s+SMS-Информирование\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:([^ ]*)";
        String smsComissionEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+komissii\\s+za\\s+pol'zovanie\\s+uslugoi\\s+SMS-Informirovanie\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:([^ ]*)";

        Map<Integer, Parser> goodsWithdrawMap = new HashMap<Integer, Parser>();
        goodsWithdrawMap.put(1, new DateParser(new SimpleDateFormat("d-M-y k:m")));
        goodsWithdrawMap.put(2, new CardParser());
        goodsWithdrawMap.put(3, new DeltaParser());
        goodsWithdrawMap.put(4, new PlaceParser());
        goodsWithdrawMap.put(5, new BalanceParser("", '.'));

        parsers.add(new REParser(goodsWithdrawMap, goodsWithdrawEn));
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
