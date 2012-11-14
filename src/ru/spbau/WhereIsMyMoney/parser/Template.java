package ru.spbau.WhereIsMyMoney.parser;

public class Template {
	private final String myTemplate;
	private final int myType;
	
	public Template(String template, int type) {
		myTemplate = template;
		myType = type;
	}
	
	public int getType() {
		return myType;
	}
	
	public String getTemplate() {
		return myTemplate;
	}
}
