package ru.spbau.WhereIsMyMoney.parser;

import android.content.Context;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplatesParser {
	private static final String TEMPLATE = "template";
	private static final String TYPE = "type";
	private static final String WITHDRAW = "withdraw";
	private static final String DEPOSIT = "deposit";
	
	private final List<Template> myTemplates = new ArrayList<Template>();
	
	public TemplatesParser(Context context) {
		XmlPullParser parser = context.getResources().getXml(R.xml.templates);
		try {
			parseXml(parser);
		} catch (Exception e) {
			Log.d(getClass().getCanonicalName(), "error while parsing templates xml");
		}
	}
	
	private void parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = parser.next();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals(TEMPLATE)) {
					int type = Transaction.NONE;
					String typeStr = parser.getAttributeValue(null, TYPE);
					if (WITHDRAW.equals(typeStr)) {
						type = Transaction.WITHDRAW;
					} else if (DEPOSIT.equals(typeStr)) {
						type = Transaction.DEPOSIT;
					}
					Template template = new Template(parser.nextText().trim(), type);
					myTemplates.add(template);
				}
			}
			eventType = parser.next();
		}
	}
	
	public List<Template> getTemplates() {
		return myTemplates;
	}
}
