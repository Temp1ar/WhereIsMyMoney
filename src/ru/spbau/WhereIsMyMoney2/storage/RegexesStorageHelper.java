package ru.spbau.WhereIsMyMoney2.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class RegexesStorageHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "regexes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String REGEXES_TABLE = "regexes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_TYPE = "integer primary key autoincrement";

    public static final String COLUMN_SERIALIZED = "serilized";
    private static final String COLUMN_SERIALIZED_TYPE = "text not null";

    private static final String CREATE_TABLE = "create table " + REGEXES_TABLE + "("
            + COLUMN_ID + " " + COLUMN_ID_TYPE + ", "
            + COLUMN_SERIALIZED + " " + COLUMN_SERIALIZED_TYPE + ");";
    private static final String DROP_TABLE = "drop table if exists " + REGEXES_TABLE;

    public RegexesStorageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(getClass().getCanonicalName(), "create database " + DATABASE_NAME);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(getClass().getCanonicalName(), "update database from "
                + oldVersion + " to " + newVersion);
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}
