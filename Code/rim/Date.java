package rim;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date {
	
	public static String getDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format.format(cal.getTime());

		return formatted;
	
	}

}
