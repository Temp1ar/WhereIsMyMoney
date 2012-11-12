package ru.spbau.WhereIsMyMoney.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper creates (updates schema) database
 * 
 * @author kmu
 */
public class TransactionLogHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "transaction_log.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_TRANSACTION = "transactions";
	
	public static final String COLUMN_ID = "_id";
	public static final int COLUMN_ID_NUM = 0;
	private static final String COLUMN_ID_TYPE = "integer primary key autoincrement";
	
	public static final String COLUMN_CARD = "card";
	public static final int COLUMN_CARD_NUM = 1;
	private static final String COLUMN_CARD_TYPE = "text not null";
	
	public static final String COLUMN_DELTA = "delta";
	public static final int COLUMN_DELTA_NUM = 2;
	private static final String COLUMN_DELTA_TYPE = "text";
	
	public static final String COLUMN_DATE = "date";
	public static final int COLUMN_DATE_NUM = 3;
	private static final String COLUMN_DATE_TYPE = "integer not null";
	
	public static final String COLUMN_BALANCE = "balance";
	public static final int COLUMN_BALANCE_NUM = 4;
	private static final String COLUMN_BALANCE_TYPE = "real no null";
	
	public static final String COLUMN_PLACE = "place";
	public static final int COLUMN_PLACE_NUM = 5;
	private static final String COLUMN_PLACE_TYPE = "text";
	
	private static final String CREATE_TABLE = "create table " + TABLE_TRANSACTION + "("
			+ COLUMN_ID      + " " + COLUMN_ID_TYPE      + ", "
			+ COLUMN_CARD    + " " + COLUMN_CARD_TYPE    + ", "
			+ COLUMN_DELTA   + " " + COLUMN_DELTA_TYPE   + ", "
			+ COLUMN_DATE    + " " + COLUMN_DATE_TYPE    + ", "
			+ COLUMN_BALANCE + " " + COLUMN_BALANCE_TYPE + ", "
			+ COLUMN_PLACE   + " " + COLUMN_PLACE_TYPE   + ");";
	
	private static final String DROP_TABLE = "drop table if exists " + TABLE_TRANSACTION;
	
	public static final String[] ALL_COLUMNS = {
		COLUMN_ID, COLUMN_CARD, COLUMN_DELTA,
		COLUMN_DATE, COLUMN_BALANCE, COLUMN_PLACE
	};
	
	public TransactionLogHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(this.getClass().getCanonicalName(), "create " + DATABASE_NAME);
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(this.getClass().getCanonicalName(), "update database from "
				+ oldVersion + " to " + newVersion);
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

}