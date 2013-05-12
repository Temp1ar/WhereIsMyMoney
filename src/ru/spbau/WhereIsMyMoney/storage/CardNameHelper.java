package ru.spbau.WhereIsMyMoney.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: Alexander Opeykin (alexander.opeykin@gmail.com)
 * Date: 5/8/13
 * Time: 1:47 AM
 */
public class CardNameHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "card_names.db";
    private static final int VERSION = 1;

    public static final String TABLE_NAME = "card_names";

    public static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_TYPE = "TEXT PRIMARY KEY";

    public static final String COLUMN_NAME = "name";
    private static final String COLUMN_NAME_TYPE = "TEXT";

    public CardNameHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        dropTable(sqLiteDatabase);
        createTable(sqLiteDatabase);
    }

    private void createTable(SQLiteDatabase db) {
        String createTableRequest = "CREATE TABLE " + TABLE_NAME+ "("
                + COLUMN_ID + " " + COLUMN_ID_TYPE + ", "
                + COLUMN_NAME + " " + COLUMN_NAME_TYPE + ");";

        db.execSQL(createTableRequest);
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IS EXISTS " + TABLE_NAME);
    }
}
