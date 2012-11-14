package ru.spbau.WhereIsMyMoney.storage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Closeable;

/**
 * Base class for database adapters
 * @author kmu
 */
public class BaseDataSource implements Closeable {
	private static final int FOR_READ = 1;
	private static final int FOR_WRITE = 2;
	
	private final SQLiteOpenHelper myHelper;
	private SQLiteDatabase myDatabase;
	
	BaseDataSource(SQLiteOpenHelper helper) {
		myHelper = helper;
	}

        /**
	 * Opens database for reading or writing.
	 * 
	 * @param mode TransactionLogSource.FOR_READ to open database for reading,
	 * 		TransactionLogSource.FOR_WRITE to open database for writing
	 */
        void open(int mode) {
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
	
	public SQLiteOpenHelper getHelper() {
		return myHelper;
	}
}
