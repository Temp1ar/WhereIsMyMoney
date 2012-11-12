package ru.spbau.WhereIsMyMoney.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Group of sms parsers from one source
 * 
 * @author kmu
 */
public class SmsParserGroup {
	public static final String NAME = "name";
	public static final String SOURCE = "source";
	
	private List<SmsParserRegex> myRegexes = new ArrayList<SmsParserRegex>();
	private String myName;
	private String mySource;
	
	/**
	 * @return group description (may be null)
	 */
	public String getName() {
		return myName;
	}
	
	public void setName(String name) {
		myName = name;
	}
	
	/**
	 * @return source of group sms's (should not be null)
	 */
	public String getSource() {
		return mySource;
	}
	
	public void setSource(String source) {
		mySource = source;
	}
	
	public void addRegex(SmsParserRegex regex) {
		myRegexes.add(regex);
	}
	
	/**
	 * @return sms parsers
	 */
	public List<SmsParserRegex> getRegexes() {
		return Collections.unmodifiableList(myRegexes);
	}
}
