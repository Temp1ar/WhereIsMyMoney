package ru.spbau.WhereIsMyMoney.storage;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RegexesStorageHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "regexes.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String REGEXES_TABLE = "regexes";
	public static final String COLUMN_REGEX_ID = "_id";
	private static final String COLUMN_REGEX_ID_TYPE = "integer primary key autoincrement";
	
	public static final String COLUMN_REGEX = "reg";
	private static final String COLUMN_REGEX_TYPE = "text not null";
	
	public static final String COLUMN_CARD = "card";
	private static final String COLUMN_CARD_TYPE = "integer not null";
	
	public static final String COLUMN_BALANCE = "balance";
	private static final String COLUMN_BALANCE_TYPE = "integer not null";
	
	public static final String COLUMN_GROUP = "grp";
	public static final String COLUMN_GROUP_TYPE = "text not null";
	
	public static final String COLUMN_DATE = "date";
	private static final String COLUMN_DATE_TYPE = "integer";
	
	public static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_DESCRIPTION_TYPE = "text";
	
	public static final String COLUMN_DELTA = "delta";
	private static final String COLUMN_DELTA_TYPE = "text";
	
	public static final String COLUMN_PLACE = "place";
	private static final String COLUMN_PLACE_TYPE = "text";
	
	public static final String[] ALL_REGEX_COLUMNS = {
		COLUMN_REGEX_ID, COLUMN_REGEX, COLUMN_CARD, COLUMN_BALANCE,
		COLUMN_GROUP, COLUMN_DATE, COLUMN_DESCRIPTION, COLUMN_DELTA,
		COLUMN_PLACE
	};
	
	private static final String CREATE_REGEXES = "create table " + REGEXES_TABLE + "("
			+ COLUMN_REGEX_ID    + " " + COLUMN_REGEX_ID_TYPE    + ", "
			+ COLUMN_REGEX       + " " + COLUMN_REGEX_TYPE       + ", "
			+ COLUMN_CARD        + " " + COLUMN_CARD_TYPE        + ", "
			+ COLUMN_BALANCE     + " " + COLUMN_BALANCE_TYPE     + ", "
			+ COLUMN_GROUP       + " " + COLUMN_GROUP_TYPE       + ", "
			+ COLUMN_DATE        + " " + COLUMN_DATE_TYPE        + ", "
			+ COLUMN_DESCRIPTION + " " + COLUMN_DESCRIPTION_TYPE + ", "
			+ COLUMN_DELTA       + " " + COLUMN_DELTA_TYPE       + ", "
			+ COLUMN_PLACE       + " " + COLUMN_PLACE_TYPE       + ");";
	private static final String DROP_REGEXES = "drop table if exists " + REGEXES_TABLE;
	
	public static final String GROUPS_TABLE = "groups";
	public static final String COLUMN_GROUP_ID = "_id";
	private static final String COLUMN_GROUP_ID_TYPE = "text primary key";
	
	public static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_SOURCE_TYPE = "text not null";
	
	public static final String[] ALL_GROUP_COLUMNS = {
		COLUMN_GROUP_ID, COLUMN_SOURCE
	};
	
	private static final String CREATE_GROUPS = "create table " + GROUPS_TABLE + "("
			+ COLUMN_GROUP_ID + " " + COLUMN_GROUP_ID_TYPE + ", "
			+ COLUMN_SOURCE   + " " + COLUMN_SOURCE_TYPE   + ");";
	private static final String DROP_GROUPS = "drop table if exists " + GROUPS_TABLE;

	private RegexStorageParser myParser;
	
	public RegexesStorageHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		myParser = new RegexStorageParser(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(getClass().getCanonicalName(), "create database " + DATABASE_NAME);
		db.execSQL(CREATE_GROUPS);
		db.execSQL(CREATE_REGEXES);
		List<SmsParserGroup> groups = myParser.getParserGroups();
		for (SmsParserGroup group : groups) {
			ContentValues g = new ContentValues();
			g.put(COLUMN_GROUP_ID, group.getName());
			g.put(COLUMN_SOURCE, group.getSource());
			db.insert(GROUPS_TABLE, null, g);
			for (SmsParserRegex regex: group.getRegexes()) {
				ContentValues r = new ContentValues();
				r.put(COLUMN_REGEX, regex.getRegex());
				r.put(COLUMN_BALANCE, regex.getGroupOf(SmsParserRegex.BALANCE));
				r.put(COLUMN_CARD, regex.getGroupOf(SmsParserRegex.CARD));
				r.put(COLUMN_GROUP, group.getName());
				if (regex.containsGroupOf(SmsParserRegex.DATE)) {
					r.put(COLUMN_DATE, regex.getGroupOf(SmsParserRegex.DATE));
				}
				if (regex.containsGroupOf(SmsParserRegex.DESCRIPTION)) {
					r.put(COLUMN_DESCRIPTION, regex.getGroupOf(SmsParserRegex.DESCRIPTION));
				}
				if (regex.containsGroupOf(SmsParserRegex.DELTA)) {
					r.put(COLUMN_DELTA, regex.getGroupOf(SmsParserRegex.DELTA));
				}
				if (regex.containsGroupOf(SmsParserRegex.PLACE)) {
					r.put(COLUMN_PLACE, regex.getGroupOf(SmsParserRegex.PLACE));
				}
				db.insert(REGEXES_TABLE, null, r);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(getClass().getCanonicalName(), "update database from "
				+ oldVersion + " to " + newVersion);
		db.execSQL(DROP_GROUPS);
		db.execSQL(DROP_REGEXES);
		onCreate(db);
	}

}
