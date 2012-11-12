package ru.spbau.WhereIsMyMoney.storage;

import java.io.Closeable;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDataSource implements Closeable {
	public static final int FOR_READ = 1;
	public static final int FOR_WRITE = 2;
	
	private SQLiteOpenHelper myHelper;
	private SQLiteDatabase myDatabase;
	
	public BaseDataSource(SQLiteOpenHelper helper) {
		myHelper = helper;
	}

	/**
	 * Opens database for reading or writing.
	 * 
	 * @param mode TransactionLogSource.FOR_READ to open database for reading,
	 * 		TransactionLogSource.FOR_WRITE to open database for writing
	 */
	public void open(int mode) {
		if (mode == FOR_READ) {
			myDatabase = myHelper.getReadableDatabase();
			Log.d(getClass().getCanonicalName(), "database " + myDatabase.getPath() + " opened for read");
		} else if (mode == FOR_WRITE) {
			myDatabase = myHelper.getWritableDatabase();
			Log.d(getClass().getCanonicalName(), "database " + myDatabase.getPath() + " opened for write");
		}
	}

	/**
	 * Opens database for reading. Equivalent of open(TransactionLogSource.FOR_READ)
	 */
	public void open() {
		open(FOR_WRITE);
	}
	
	public void close() {
		myHelper.close();
		Log.d(getClass().getCanonicalName(), "database " + myDatabase.getPath() + " closed");
	}
	
	public SQLiteDatabase getDatabase() {
		return myDatabase;
	}
}
