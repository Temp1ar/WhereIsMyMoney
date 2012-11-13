package ru.spbau.WhereIsMyMoney.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.WhereIsMyMoney.parser.Parser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Parsers database adapter
 * 
 * @author kmu
 */
public class RegexesStorageSource extends BaseDataSource {
	public RegexesStorageSource(Context context) {
		super(new RegexesStorageHelper(context));
	}
	
	public void addSmsParser(Parser parser) throws IOException {
		ContentValues g = new ContentValues();
		g.put(RegexesStorageHelper.COLUMN_SERIALIZED, serializeParser(parser));
		Log.d(getClass().getCanonicalName(), "Parser " + parser + " serialized to database");
	}
	
	public List<Parser> getSmsParsers()
			throws StreamCorruptedException, IOException, ClassNotFoundException {
		List<Parser> parsers = new ArrayList<Parser>();
		Cursor cursor = getDatabase().query(RegexesStorageHelper.REGEXES_TABLE,
				RegexesStorageHelper.ALL_COLUMNS,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String serialized = cursor.getString(cursor.getColumnIndex(RegexesStorageHelper.COLUMN_SERIALIZED));
			parsers.add(deserializeParser(serialized));
		}
		return parsers;
	}
	
	public static String serializeParser(Parser parser) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytes);
		out.writeObject(parser);
		out.flush();
		String serialized = bytes.toString();
		out.close();
		return serialized;
	}
	
	public static Parser deserializeParser(String serialized)
			throws StreamCorruptedException, IOException, ClassNotFoundException {
		ByteArrayInputStream bytes = new ByteArrayInputStream(serialized.getBytes());
		ObjectInputStream in = new ObjectInputStream(bytes);
		Parser parser = (Parser) in.readObject();
		in.close();
		return parser;
	}
}
