package ru.spbau.WhereIsMyMoney.storage;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class RegexesStorageSource extends BaseDataSource {
	public RegexesStorageSource(Context context) {
		super(new RegexesStorageHelper(context));
	}
	
	public void addGroup(SmsParserGroup group) {
		ContentValues g = new ContentValues();
		g.put(RegexesStorageHelper.COLUMN_GROUP_ID, group.getName());
		g.put(RegexesStorageHelper.COLUMN_SOURCE, group.getSource());
		long id = getDatabase().insert(RegexesStorageHelper.GROUPS_TABLE, null, g);
		Log.d(getClass().getCanonicalName(), "SmsRegexGroup " + group + " saved with id " + id);
	}
	
	private void addRegex(SmsParserGroup group, SmsParserRegex regex) {
		ContentValues r = new ContentValues();
		r.put(RegexesStorageHelper.COLUMN_REGEX, regex.getRegex());
		r.put(RegexesStorageHelper.COLUMN_CARD, regex.getGroupOf(SmsParserRegex.CARD));
		r.put(RegexesStorageHelper.COLUMN_BALANCE, regex.getGroupOf(SmsParserRegex.BALANCE));
		r.put(RegexesStorageHelper.COLUMN_GROUP, group.getName());
		if (regex.getDescription() != null) {
			r.put(RegexesStorageHelper.COLUMN_DESCRIPTION, regex.getDescription());
		}
		if (regex.containsGroupOf(SmsParserRegex.DATE)) {
			r.put(RegexesStorageHelper.COLUMN_DATE, regex.getGroupOf(SmsParserRegex.DATE));
		}
		if (regex.containsGroupOf(SmsParserRegex.DELTA)) {
			r.put(RegexesStorageHelper.COLUMN_DELTA, regex.getGroupOf(SmsParserRegex.DELTA));
		}
		if (regex.containsGroupOf(SmsParserRegex.PLACE)) {
			r.put(RegexesStorageHelper.COLUMN_PLACE, regex.getGroupOf(SmsParserRegex.PLACE));
		}
		long id = getDatabase().insert(RegexesStorageHelper.REGEXES_TABLE, null, r);
		Log.d(getClass().getCanonicalName(), "SmsRegexRegex " + regex + " saved with id " + id);
	}
	
	public List<SmsParserGroup> getSmsParserGroups(IFilter<SmsParserGroup> filter) {
		List<SmsParserGroup> groups = new ArrayList<SmsParserGroup>();
		Cursor cursor = getDatabase().query(RegexesStorageHelper.GROUPS_TABLE,
				RegexesStorageHelper.ALL_GROUP_COLUMNS,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SmsParserGroup group = cursorToGroup(cursor);
			if ((filter == null) || filter.match(group)) {
				groups.add(group);
			}
		}
		cursor.close();
		return groups;
	}
	
	public List<SmsParserGroup> getAllSmsParserGroups() {
		return getSmsParserGroups(null);
	}
	
	private SmsParserGroup cursorToGroup(Cursor cursor) {
		SmsParserGroup group = new SmsParserGroup();
		group.setName(cursor.getString(cursor.getColumnIndex(RegexesStorageHelper.COLUMN_GROUP_ID)));
		group.setSource(cursor.getString(cursor.getColumnIndex(RegexesStorageHelper.COLUMN_SOURCE)));
		return group;
	}
	
	public void getRegexesForGroup(IFilter<SmsParserRegex> filter, SmsParserGroup group) {
		Cursor cursor = getDatabase().query(RegexesStorageHelper.REGEXES_TABLE,
				RegexesStorageHelper.ALL_REGEX_COLUMNS,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SmsParserRegex regex = cursorToRegex(cursor);
			if ((filter == null) || filter.match(regex)) {
				group.addRegex(regex);
			}
		}
		cursor.close();		
	}
	
	public void getRegexesForGroup(SmsParserGroup group) {
		getRegexesForGroup(null, group);
	}
	
	private SmsParserRegex cursorToRegex(Cursor cursor) {
		SmsParserRegex regex = new SmsParserRegex();
		regex.setRegex(cursor.getString(cursor.getColumnIndex(RegexesStorageHelper.COLUMN_REGEX)));
		regex.setGroupOf(SmsParserRegex.BALANCE, cursor.getInt(
				cursor.getColumnIndex(RegexesStorageHelper.COLUMN_BALANCE)));
		regex.setGroupOf(SmsParserRegex.CARD, cursor.getInt(
				cursor.getColumnIndex(RegexesStorageHelper.COLUMN_CARD)));
		int column = cursor.getColumnIndex(RegexesStorageHelper.COLUMN_DATE);
		if (!cursor.isNull(column)) {
			regex.setGroupOf(SmsParserRegex.DATE, cursor.getInt(column));
		}
		column = cursor.getColumnIndex(RegexesStorageHelper.COLUMN_DELTA);
		if (!cursor.isNull(column)) {
			regex.setGroupOf(SmsParserRegex.DELTA, cursor.getInt(column));
		}
		column = cursor.getColumnIndex(RegexesStorageHelper.COLUMN_DESCRIPTION);
		if (!cursor.isNull(column)) {
			regex.setGroupOf(SmsParserRegex.DESCRIPTION, cursor.getInt(column));
		}
		column = cursor.getColumnIndex(RegexesStorageHelper.COLUMN_PLACE);
		if (!cursor.isNull(column)) {
			regex.setGroupOf(SmsParserRegex.PLACE, cursor.getInt(column));
		}
		return regex;
	}
}
