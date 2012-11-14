package ru.spbau.WhereIsMyMoney.parser;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateMatcher {
	private static final Pattern TEMPLATE_ARG = Pattern.compile("\\{\\{(.*?)\\}\\}");
	private static final Pattern SPACE_FILLER = Pattern.compile("\\s+");
	private static final String GROUP_RE = "(.*)";
	private static final String SPACE_RE = "\\\\s*";
	private final Map<String, Integer> myMapping = new HashMap<String, Integer>();
	private String myRegex;
	
	public TemplateMatcher(String template) {
		Matcher matcher = TEMPLATE_ARG.matcher(template);
		int occurence = 0;

        Map<String,String> reMap = new HashMap<String, String>();

		while (matcher.find()) {
			String arg = matcher.group(1);
            String[] splitted = arg.split("\\:", 2);
            if (splitted.length == 2) {
                reMap.put(arg, "(" + splitted[1] + ")");
            } else {
                reMap.put(arg, GROUP_RE);
            }
			myMapping.put(splitted[0], occurence + 1);
			++occurence;
		}

        myRegex = template;

        for (Map.Entry<String, String> entry : reMap.entrySet()) {
        	String placeholder = "{{" + entry.getKey() + "}}";
            String re = entry.getValue();
            myRegex = plainReplace(myRegex, placeholder, re);
        }

        matcher = SPACE_FILLER.matcher(myRegex);
		myRegex = matcher.replaceAll(SPACE_RE);
        Log.d(getClass().getCanonicalName(), myRegex);
	}

	private String plainReplace(String str, String value, String replacement) {
		int index = str.indexOf(value);
		return str.substring(0, index) + replacement + str.substring(index + value.length());
	}

	public Map<String, String> match(String string) {
		Pattern pattern = Pattern.compile(myRegex);
		Matcher matcher = pattern.matcher(string);
		if (matcher.matches()) {
			Map<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, Integer> entry : myMapping.entrySet()) {
				map.put(entry.getKey(), matcher.group(entry.getValue()));
			}
			return map;
		}
		return null;
	}
}
