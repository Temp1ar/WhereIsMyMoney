package ru.spbau.WhereIsMyMoney.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * User: Alexander Opeykin (alexander.opeykin@gmail.com)
 * Date: 5/8/13
 * Time: 2:11 AM
 */
public class CardNameSource {
    CardNameHelper helper;

    public CardNameSource(Context context) {
        helper = new CardNameHelper(context);
    }

    public String getName(String id) {
        SQLiteDatabase dbR = helper.getReadableDatabase();

        String query = "SELECT " + CardNameHelper.COLUMN_NAME +
                " FROM " + CardNameHelper.TABLE_NAME +
                " WHERE " + CardNameHelper.COLUMN_ID + " = '" + id + "'";

        Cursor cursor = dbR.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            return null;
        }

        String name = cursor.getString(0);
        cursor.close();
        dbR.close();
        return name;
    }

    public void setName(String id, String name) {
        SQLiteDatabase dbW = helper.getWritableDatabase();

        ContentValues contentValues = createContentValues(id, name);

        if (!updateName(id, contentValues, dbW)) {
            insertName(contentValues, dbW);
        }

        dbW.close();
    }

    public void close() {
        helper.close();
    }

    private ContentValues createContentValues(String id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardNameHelper.COLUMN_ID, id);
        contentValues.put(CardNameHelper.COLUMN_NAME, name);
        return contentValues;
    }

    private void insertName(ContentValues contentValues, SQLiteDatabase dbW) {
        dbW.insert(CardNameHelper.TABLE_NAME, null, contentValues);
    }

    private boolean updateName(String id, ContentValues contentValues, SQLiteDatabase dbW) {
        String whereClause = CardNameHelper.COLUMN_ID + " = '" + id + "'";

        try {
            int rowAffected = dbW.update(CardNameHelper.TABLE_NAME, contentValues, whereClause, null);
            return rowAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}