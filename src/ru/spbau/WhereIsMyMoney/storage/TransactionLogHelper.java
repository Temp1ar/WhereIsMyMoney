package ru.spbau.WhereIsMyMoney.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.spbau.WhereIsMyMoney.ExistingSmsReader;
import ru.spbau.WhereIsMyMoney.SmsEvent;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.parser.SmsParser;

import java.util.ArrayList;
import java.util.Date;

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
	
	private static final String COLUMN_ID = "_id";
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
            ArrayList<SmsEvent> array = ExistingSmsReader.getAll(context);
    
            //BaltBankHelper baltBankHelper = new BaltBankHelper();
            SmsParser parser = new SmsParser(context);
            //Transaction transaction;
        for (SmsEvent anArray : array) {
            //    if ((transaction = baltBankHelper.tryParse(array.get(i).getBody())) != null) {
            //        addTransaction(transaction, db);
            //        continue;
            //    }
            Transaction trans = parser.parseSms(anArray);
            if (trans != null) {
                addTransaction(trans, db);
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