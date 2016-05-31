package com.matteo.cc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class XTimeUtils {
	static final String[] WEEK_DAYS = { "星期日", "星期一", "星期二", "星期三", "星期四",
			"星期五", "星期六" };
	static SimpleDateFormat DateFormatter = new SimpleDateFormat("M月d日",
			Locale.getDefault());

	public static String formatDate(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
		String displayDate = String.format("%s %s",
				DateFormatter.format(dt), WEEK_DAYS[w]);
		return displayDate;
	}
}
