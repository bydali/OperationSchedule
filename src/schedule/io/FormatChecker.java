package schedule.io;

import java.util.regex.Pattern;

public class FormatChecker {
	public static boolean checkTimeFormat(String time) {
		try {
			String pattern = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}";
			boolean isMatch = Pattern.matches(pattern, time);
			return isMatch;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
