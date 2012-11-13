package ru.spbau.WhereIsMyMoney.storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.spbau.WhereIsMyMoney.ExistingSmsReader;
import ru.spbau.WhereIsMyMoney.SmsEvent;
import ru.spbau.WhereIsMyMoney.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.spbau.WhereIsMyMoney.parser.*;

/**
 * Helper creates (updates schema) database
 * 
 * @author kmu
 */
public class TransactionLogHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "transaction_log.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String CASH = "CASH";
	
	public static final String TABLE_TRANSACTION = "transactions";
	
	public static final String COLUMN_ID = "_id";
	private static final String COLUMN_ID_TYPE = "integer primary key autoincrement";
	
	public static final String COLUMN_CARD = "card";
	private static final String COLUMN_CARD_TYPE = "text not null";
	
	public static final String COLUMN_DATE = "date";
	private static final String COLUMN_DATE_TYPE = "integer not null";
	
	public static final String COLUMN_BALANCE = "balance";
	private static final String COLUMN_BALANCE_TYPE = "real not null";
	
	public static final String COLUMN_TYPE = "type";
	private static final String COLUMN_TYPE_TYPE = "integer not null";
	
	public static final String COLUMN_DELTA = "delta";
	private static final String COLUMN_DELTA_TYPE = "text";
	
	public static final String COLUMN_PLACE = "place";
	private static final String COLUMN_PLACE_TYPE = "text";
	
	private static final String CREATE_TABLE = "create table " + TABLE_TRANSACTION + "("
			+ COLUMN_ID      + " " + COLUMN_ID_TYPE      + ", "
			+ COLUMN_CARD    + " " + COLUMN_CARD_TYPE    + ", "
			+ COLUMN_DATE    + " " + COLUMN_DATE_TYPE    + ", "
			+ COLUMN_BALANCE + " " + COLUMN_BALANCE_TYPE + ", "
			+ COLUMN_TYPE    + " " + COLUMN_TYPE_TYPE    + ", "
			+ COLUMN_DELTA   + " " + COLUMN_DELTA_TYPE   + ", "
			+ COLUMN_PLACE   + " " + COLUMN_PLACE_TYPE   + ");";
	
	private static final String DROP_TABLE = "drop table if exists " + TABLE_TRANSACTION;
	
	public static final String[] ALL_COLUMNS = {
		COLUMN_ID, COLUMN_CARD, COLUMN_DATE, COLUMN_TYPE,
		COLUMN_BALANCE, COLUMN_DELTA, COLUMN_PLACE
	};
    private final Context context;

    public TransactionLogHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
                this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(getClass().getCanonicalName(), "create " + DATABASE_NAME);
		db.execSQL(CREATE_TABLE);
		insertCash(db);
                insertExistingSms(db);
	}

    private void insertExistingSms(SQLiteDatabase db) {
        Log.d(getClass().getCanonicalName(), "inserting existing sms");
        ArrayList<SmsEvent> array = ExistingSmsReader.getAll(context, null);

        String bankCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+средств\\s+со\\s+счета\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:(.*)";
        String bankCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+sredstv\\s+so\\s+scheta\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:(.*)";
        String atmCashout = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Получение\\s+наличных\\s+в\\s+банкомате\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:(.*)";
        String atmCashoutEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Poluchenie\\s+nalichnykh\\s+v\\s+bankomate\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:(.*)";
        String salaryDeposit = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Поступление\\s+зарплаты\\s+([^\\s]*\\s+[^;]*);Доступно:(.*)";
        String salaryDepositEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Postuplenie\\s+zarplaty\\s+([^\\s]*\\s+[^;]*);Dostupno:(.*)";
        String goodsWithdraw = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Оплата\\s+товаров/услуг\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:(.*)";
        String goodsWithdrawEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Oplata\\s+tovarov/uslug\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:(.*)";
        String smsComission = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Списание\\s+комиссии\\s+за\\s+пользование\\s+услугой\\s+SMS-Информирование\\s+([^\\s]*\\s+[^\\s]*)\\s+код\\s+[^\\s]*?\\s+([^;]*?);Доступно:(.*)";
        String smsComissionEn = "([^\\s]*\\s+[^\\s]*)\\s+([^:]*?):\\s+Spisanie\\s+komissii\\s+za\\s+pol'zovanie\\s+uslugoi\\s+SMS-Informirovanie\\s+([^\\s]*\\s+[^\\s]*)\\s+kod\\s+[^\\s]*?\\s+([^;]*?);Dostupno:(.*)";

        Map<Integer, Parser> small = new HashMap<Integer, Parser>();
        small.put(1, new DateParser(new SimpleDateFormat("d-M-y k:m")));
        small.put(2, new CardParser());
        small.put(3, new DeltaParser());
        small.put(4, new PlaceParser());
        small.put(5, new BalanceParser("", '.'));

        REParser parser = new REParser(small, goodsWithdraw);


        for (int i = 0; i < array.size(); ++i) {
            Pattern pattern;
            pattern = Pattern.compile(goodsWithdraw);
            Matcher m = pattern.matcher(array.get(i).getBody());
            m.find();
            if (m.matches()) {
                Log.d(getClass().getCanonicalName(), "Matched: " + array.get(i).getBody());
            }
            Transaction transaction = new Transaction();
            if (parser.parse(array.get(i).getBody(), transaction)) {
                Log.d(getClass().getCanonicalName(), "Parsed: " + array.get(i).getBody());
                addTransaction(transaction, db);
            } else {
            }
        }
    }

    private void insertCash(SQLiteDatabase db) {
		ContentValues cash = new ContentValues();
		cash.put(COLUMN_CARD, CASH);
		cash.put(COLUMN_DELTA, "0");
		cash.put(COLUMN_DATE, (new Date()).getTime());
		cash.put(COLUMN_BALANCE, 0);
		cash.put(COLUMN_TYPE, Transaction.DEPOSIT);
		db.insert(TABLE_TRANSACTION, null, cash);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(getClass().getCanonicalName(), "update database from "
				+ oldVersion + " to " + newVersion);
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

    public static void addTransaction(Transaction transaction, SQLiteDatabase db) {
            ContentValues dbTransaction = new ContentValues();
            dbTransaction.put(TransactionLogHelper.COLUMN_CARD, transaction.getCard());
            dbTransaction.put(TransactionLogHelper.COLUMN_DELTA, transaction.getDelta());
            dbTransaction.put(TransactionLogHelper.COLUMN_DATE, transaction.getDate().getTime());
            dbTransaction.put(TransactionLogHelper.COLUMN_BALANCE, transaction.getBalance());
            dbTransaction.put(TransactionLogHelper.COLUMN_TYPE, transaction.getType());
            dbTransaction.put(TransactionLogHelper.COLUMN_PLACE, transaction.getPlace());

            long insertId = db.insert(TransactionLogHelper.TABLE_TRANSACTION, null, dbTransaction);

            Log.d(RegexesStorageHelper.class.getCanonicalName(), "Transaction " + transaction + " saved with id " + insertId);
        }
}