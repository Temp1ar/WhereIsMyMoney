package ru.spbau.WhereIsMyMoney.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Sms parser description
 * 
 * @author kmu
 */
public class SmsParserRegex {
	public static final String DESCRIPTION = "description";
	public static final String REGEX = "regex";
	public static final String CARD = "card";
	public static final String BALANCE = "balance";
	public static final String DATE = "date";
	public static final String DELTA = "delta";
	public static final String PLACE = "place";
	
	private Map<String, Integer> myAttributes = new HashMap<String, Integer>();
	private String myDescription;
	private String myRegex;
	
	/**
	 * @return sms regex
	 */
	public String getRegex() {
		return myRegex;
	}
	
	public void setRegex(String regex) {
		myRegex = regex;
	}
	
	/**
	 * @return parser description
	 */
	public String getDescription() {
		return myDescription;
	}
	
	public void setDescription(String description) {
		myDescription = description;
	}
	
	public boolean containsGroupOf(String groupName) {
		return myAttributes.containsKey(groupName);
	}
	
	/**
	 * @param groupName name of sms field
	 * @return group number in regex
	 */
	public int getGroupOf(String groupName) {
		return myAttributes.get(groupName);
	}
	
	public void setGroupOf(String groupName, int groupNo) {
		myAttributes.put(groupName, groupNo);
	}
}
