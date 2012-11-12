package ru.spbau.WhereIsMyMoney.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ru.spbau.WhereIsMyMoney.R;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * Parses predefined SMS regexes from res/xml/regexes
 * 
 * @author kmu
 */
public class RegexStorageParser {
	private static final String GROUP = "ParserGroup";
	private static final String PARSER = "ParserRegex";
	
	private List<SmsParserGroup> myGroups = new ArrayList<SmsParserGroup>();
	private XmlResourceParser myParser;
	
	public RegexStorageParser(Context context) {
		myParser = context.getResources().getXml(R.xml.regexes);
		try {
			parseRegexes();
		} catch (Exception e) {
			Log.w(getClass().getCanonicalName(), "error while parsing xml: " + e.getLocalizedMessage());
			myGroups.clear();
			myParser.close();
		}
	}
	
	private void parseRegexes() throws XmlPullParserException, IOException {
		SmsParserGroup group = null;
		int eventType = myParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_TAG: {
					String tag = myParser.getName();
					if (tag == GROUP) {
						group = new SmsParserGroup();
						parseGroup(group);
					} else if (tag == PARSER) {
						SmsParserRegex regex = new SmsParserRegex();
						parseRegex(regex);
						group.addRegex(regex);
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					String tag = myParser.getName();
					if (tag == GROUP) {
						myGroups.add(group);
						group = null;
					}
					break;
				}
			}
			eventType = myParser.next();
		}
	}
	
	private void parseRegex(SmsParserRegex regex) throws XmlPullParserException {
		for (int i = 0; i < myParser.getAttributeCount(); ++i) {
			String attribute = myParser.getAttributeName(i);
			if (attribute == SmsParserRegex.DESCRIPTION) {
				regex.setDescription(myParser.getAttributeValue(i));
			} else if (attribute == SmsParserRegex.REGEX) {
				regex.setRegex(myParser.getAttributeValue(i));
			} else if (attribute == SmsParserRegex.CARD) {
				regex.setGroupOf(SmsParserRegex.CARD, myParser.getAttributeIntValue(i, 0));
			} else if (attribute == SmsParserRegex.BALANCE) {
				regex.setGroupOf(SmsParserRegex.BALANCE, myParser.getAttributeIntValue(i, 0));
			} else if (attribute == SmsParserRegex.DATE) {
				regex.setGroupOf(SmsParserRegex.DATE, myParser.getAttributeIntValue(i, 0));
			} else if (attribute == SmsParserRegex.DELTA) {
				regex.setGroupOf(SmsParserRegex.DELTA, myParser.getAttributeIntValue(i, 0));
			} else if (attribute == SmsParserRegex.PLACE) {
				regex.setGroupOf(SmsParserRegex.PLACE, myParser.getAttributeIntValue(i, 0));
			} else {
				throw new XmlPullParserException("unexpected attribute " + attribute);
			}
		}
	}
	
	private void parseGroup(SmsParserGroup group) throws XmlPullParserException {
		for (int i = 0; i < myParser.getAttributeCount(); ++i) {
			String attribute = myParser.getAttributeName(i);
			if (attribute == SmsParserGroup.NAME) {
				group.setName(myParser.getAttributeValue(i));
			} else if (attribute == SmsParserGroup.SOURCE) {
				group.setSource(myParser.getAttributeValue(i));
			} else {
				throw new XmlPullParserException("unexpected attribute " + attribute);
			}
		}
	}
	
	/**
	 * List of SMS parsers groups
	 * 
	 * @return groups of parsers
	 */
	public List<SmsParserGroup> getParserGroups() {
		return myGroups;
	}
}
