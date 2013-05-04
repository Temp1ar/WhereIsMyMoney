package ru.spbau.WhereIsMyMoney.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public static final String TRANSACTIONS_TABLE_NAME = "transactions";

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


    public static final String[] ALL_COLUMNS = {
            COLUMN_ID, COLUMN_CARD, COLUMN_DATE, COLUMN_TYPE,
            COLUMN_BALANCE, COLUMN_DELTA, COLUMN_PLACE
    };

    private final Context context;

    public TransactionLogHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private void createTable(SQLiteDatabase db) {
        String createTableRequest = "create table " + TRANSACTIONS_TABLE_NAME + "("
                + COLUMN_ID + " " + COLUMN_ID_TYPE + ", "
                + COLUMN_CARD + " " + COLUMN_CARD_TYPE + ", "
                + COLUMN_DATE + " " + COLUMN_DATE_TYPE + ", "
                + COLUMN_BALANCE + " " + COLUMN_BALANCE_TYPE + ", "
                + COLUMN_TYPE + " " + COLUMN_TYPE_TYPE + ", "
                + COLUMN_DELTA + " " + COLUMN_DELTA_TYPE + ", "
                + COLUMN_PLACE + " " + COLUMN_PLACE_TYPE + ");";

        db.execSQL(createTableRequest);
    }

    public void processExistingSmses(SQLiteDatabase db, SmsParser parser) {
        Log.d(getClass().getCanonicalName(), "inserting existing sms");
        ArrayList<SmsEvent> smsEvents = ExistingSmsReader.getAll(context);

        for (SmsEvent smsEvent : smsEvents) {
            Transaction trans = parser.parseSms(smsEvent);
            if (trans != null) {
                addTransaction(trans, db);
            }
        }
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TRANSACTIONS_TABLE_NAME);
    }

    public static void addTransaction(Transaction transaction, SQLiteDatabase db) {
        ContentValues dbTransaction = new ContentValues();
        dbTransaction.put(COLUMN_CARD, transaction.getCard());
        dbTransaction.put(COLUMN_DELTA, transaction.getDelta());
        dbTransaction.put(COLUMN_DATE, transaction.getDate().getTime());
        dbTransaction.put(COLUMN_BALANCE, transaction.getBalance());
        dbTransaction.put(COLUMN_TYPE, transaction.getType());
        dbTransaction.put(COLUMN_PLACE, transaction.getPlace());

        long insertId = db.insert(TRANSACTIONS_TABLE_NAME, null, dbTransaction);

        Log.d(RegexesStorageHelper.class.getCanonicalName(), "Transaction " + transaction + " saved with id " + insertId);
    }

    public static Date getLatestProcessedSmsDate(SQLiteDatabase db) {
        if (getTransactionsTableSize(db) == 0)
            return new Date(0);

        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_DATE + ") FROM " + TRANSACTIONS_TABLE_NAME, null);
        cursor.moveToFirst();
        long lastProcessedSmsDate = cursor.getLong(0);
        cursor.close();
        return new Date(lastProcessedSmsDate);
    }

    private static int getTransactionsTableSize(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + TRANSACTIONS_TABLE_NAME, null);
        cursor.moveToFirst();
        int size = cursor.getInt(0);
        cursor.close();
        return size;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(getClass().getCanonicalName(), "create " + DATABASE_NAME);
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(getClass().getCanonicalName(), "update database from "
                + oldVersion + " to " + newVersion);

        dropTable(db);
        createTable(db);
    }
}