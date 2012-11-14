package ru.spbau.WhereIsMyMoney.storage;

import ru.spbau.WhereIsMyMoney.parser.Template;
import ru.spbau.WhereIsMyMoney.parser.TemplatesParser;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TemplatesHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "templates.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_TEMPLATES = "templates";
	
	public static final String COLUMN_ID = "_id";
	private static final String COLUMN_ID_TYPE = "integer primary key autoincrement";
	
	public static final String COLUMN_TEMPLATE = "template";
	private static final String COLUMN_TEMPLATE_TYPE = "text not null";
	
	public static final String COLUMN_TYPE = "type";
	private static final String COLUMN_TYPE_TYPE = "integer not null";
	
	public static final String[] ALL_COLUMNS = {
		COLUMN_ID, COLUMN_TEMPLATE, COLUMN_TYPE
	};
	
	private static final String CREATE_TABLE = "create table " + TABLE_TEMPLATES + "("
			+ COLUMN_ID       + " " + COLUMN_ID_TYPE       + ", "
			+ COLUMN_TEMPLATE + " " + COLUMN_TEMPLATE_TYPE + ", "
			+ COLUMN_TYPE     + " " + COLUMN_TYPE_TYPE     + ");";
	
	private static final String DROP_TABLE = "drop table if exists " + TABLE_TEMPLATES;
	
	private final Context myContext;
	
	public TemplatesHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		myContext = context;
	}

	public static void addTemplate(Template template, SQLiteDatabase db) {
		ContentValues record = new ContentValues();
		record.put(COLUMN_TEMPLATE, template.getTemplate());
		record.put(COLUMN_TYPE, template.getType());
		long insertId = db.insert(TABLE_TEMPLATES, null, record);
		Log.d(TemplatesHelper.class.getCanonicalName(), "template inserted with id " + insertId);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(getClass().getCanonicalName(), "create " + DATABASE_NAME);
		db.execSQL(CREATE_TABLE);
		
		TemplatesParser parser = new TemplatesParser(myContext);
		for (Template template : parser.getTemplates()) {
			addTemplate(template, db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(getClass().getCanonicalName(), "update database from "
				+ oldVersion + " to " + newVersion);
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
