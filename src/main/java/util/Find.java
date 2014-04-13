package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Find {
	
	public static List<String> findLines(String contentDate, String start, String end) {
		List<String> lines = new ArrayList<String>();
		int index = 0;
		while (index <= contentDate.length()) {
			int t0 = contentDate.indexOf(start, index);
			index = t0 + start.length();
			int t1 = contentDate.indexOf(end, index);
			index = t1 + end.length();
			if (t0 == -1 || t1 == -1) {
				return lines;
			}
			lines.add(contentDate.substring(t0, t1));
			index++;
		}
		return lines;
	}
	
	public static String findRegex(String contentDate, String pattern) {
		Pattern MY_PATTERN = Pattern.compile(pattern);
		Matcher m = MY_PATTERN.matcher(contentDate);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

}
