package ru.spbau.WhereIsMyMoney2.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ru.spbau.WhereIsMyMoney2.parser.Template;
import ru.spbau.WhereIsMyMoney2.parser.TemplatesParser;

class TemplatesHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "templates.db";
    private static final int DATABASE_VERSION = 1;

    static final String TEMPLATES_TABLE_NAME = "templates";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_ID_TYPE = "integer primary key autoincrement";

    static final String COLUMN_TEMPLATE = "template";
    static final String COLUMN_TEMPLATE_TYPE = "text not null";

    static final String COLUMN_TYPE = "type";
    static final String COLUMN_TYPE_TYPE = "integer not null";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID, COLUMN_TEMPLATE, COLUMN_TYPE
    };

    private final Context context;

    public TemplatesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static void addTemplate(Template template, SQLiteDatabase db) {
        ContentValues record = new ContentValues();
        record.put(COLUMN_TEMPLATE, template.getTemplate());
        record.put(COLUMN_TYPE, template.getType());
        long insertId = db.insert(TEMPLATES_TABLE_NAME, null, record);
        Log.d(TemplatesHelper.class.getCanonicalName(), "template inserted with id " + insertId);
    }

    private void fillTable(SQLiteDatabase db) {
        TemplatesParser parser = new TemplatesParser(context);
        for (Template template : parser.getTemplates()) {
            addTemplate(template, db);
        }
    }

    private void createTable(SQLiteDatabase db) {
        Log.d(getClass().getCanonicalName(), "create " + DATABASE_NAME);

        String createTableRequest = "create table " + TEMPLATES_TABLE_NAME + "("
                + COLUMN_ID + " " + COLUMN_ID_TYPE + ", "
                + COLUMN_TEMPLATE + " " + COLUMN_TEMPLATE_TYPE + ", "
                + COLUMN_TYPE + " " + COLUMN_TYPE_TYPE + ");";

        db.execSQL(createTableRequest);
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TEMPLATES_TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        fillTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(getClass().getCanonicalName(), "update database from "
                + oldVersion + " to " + newVersion);

        dropTable(db);
        createTable(db);
        fillTable(db);
    }
}
