package ru.spbau.WhereIsMyMoney2.storage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Closeable;

/**
 * Base class for database adapters
 *
 * @author kmu
 */
public class BaseDataSource implements Closeable {
    private static final int FOR_READ = 1;
    private static final int FOR_WRITE = 2;

    private final SQLiteOpenHelper helper;
    private SQLiteDatabase database;

    BaseDataSource(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /**
     * Opens database for reading or writing.
     *
     * @param mode TransactionLogSource.FOR_READ to open database for reading,
     *             TransactionLogSource.FOR_WRITE to open database for writing
     */
    void open(int mode) {
        if (mode == FOR_READ) {
            database = helper.getReadableDatabase();
            Log.d(getClass().getCanonicalName(), "database " + database.getPath() + " opened for read");
        } else if (mode == FOR_WRITE) {
            database = helper.getWritableDatabase();
            Log.d(getClass().getCanonicalName(), "database " + database.getPath() + " opened for write");
        }
    }

    public void resetDatabase() {
        getHelper().onUpgrade(getDatabase(), 1, 1);
    }

    /**
     * Opens database for reading. Equivalent of open(TransactionLogSource.FOR_READ)
     */
    public void open() {
        open(FOR_WRITE);
    }

    public void close() {
        helper.close();
        Log.d(getClass().getCanonicalName(), "database " + database.getPath() + " closed");
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    SQLiteOpenHelper getHelper() {
        return helper;
    }
}
