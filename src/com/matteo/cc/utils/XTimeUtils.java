package com.matteo.cc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class XTimeUtils {
	static final String[] CHINESE_NUMBERS = { "一", "二", "三" };
	static final String[] WEEK_DAYS = { "星期日", "星期一", "星期二", "星期三", "星期四",
			"星期五", "星期六" };
	static final long MILLISECONDS_ONE_DAY = 24 * 60 * 60 * 1000;
	static final long LATELY_DAYS_COUNT = 3;
	static SimpleDateFormat DateFormatter = new SimpleDateFormat("M月d日",
			Locale.getDefault());
	static SimpleDateFormat TodayDateFormatter = new SimpleDateFormat("H:m",
			Locale.getDefault());

	public static String formatDate(Date dt) {
		Date dtNow = new Date(System.currentTimeMillis());
		long todayTime = ((long) dtNow.getTime() / MILLISECONDS_ONE_DAY)
				* MILLISECONDS_ONE_DAY;

		if (dt.getTime() >= todayTime) {
			return TodayDateFormatter.format(dt);
		}
		long latelyTime = todayTime - LATELY_DAYS_COUNT * MILLISECONDS_ONE_DAY;
		if (dt.getTime() >= latelyTime) {
			long duration = dt.getTime() - latelyTime;
			int d = (int) (LATELY_DAYS_COUNT - duration / MILLISECONDS_ONE_DAY - 1);
			return CHINESE_NUMBERS[d] + "天前";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		String displayDate = String.format("%s %s", DateFormatter.format(dt),
				WEEK_DAYS[w]);
		return displayDate;
	}

	public static boolean isSameToday(Date dt1, Date dt2) {
		return (dt1.getTime() / MILLISECONDS_ONE_DAY) == (dt2.getTime() / MILLISECONDS_ONE_DAY);
	}
}
