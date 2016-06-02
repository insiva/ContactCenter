package com.matteo.cc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class XTimeUtils {
	static final String[] CHINESE_NUMBERS = { "一", "二", "三" };
	static final String[] WEEK_DAYS = { "星期日", "星期一", "星期二", "星期三", "星期四",
			"星期五", "星期六" };
	static final String TODAY="今天";
	static final String DAYS_BEFORE="天前";
	static final String HOUR="小时";
	static final String MINUTE="分";
	static final String SECOND="秒";
	static final long MILLISECONDS_ONE_DAY = 24 * 60 * 60 * 1000;
	static final long MILLISECONDS_ONE_MINUTE= 60 * 1000;
	static final long LATELY_DAYS_COUNT = 3;
	static final long SECONDS_ONE_MINUTE=60;
	static final long SECONDS_ONE_HOUR=3600;
	static SimpleDateFormat DayFormatter = new SimpleDateFormat("M月d日",
			Locale.getDefault());
	static SimpleDateFormat DateFormatter = new SimpleDateFormat("M月d日 H:m",
			Locale.getDefault());
	static SimpleDateFormat TodayDateFormatter = new SimpleDateFormat("H:m",
			Locale.getDefault());
	static SimpleDateFormat TimeDateFormatter = new SimpleDateFormat("HH:mm:ss",
			Locale.getDefault());
	
	public static String formatDay(Date dt){
		Date dtNow = new Date(System.currentTimeMillis());
		long todayTime = ((long) dtNow.getTime() / MILLISECONDS_ONE_DAY)
				* MILLISECONDS_ONE_DAY;

		if (dt.getTime() >= todayTime) {
			return TODAY;
		}
		long latelyTime = todayTime - LATELY_DAYS_COUNT * MILLISECONDS_ONE_DAY;
		if (dt.getTime() >= latelyTime) {
			long duration = dt.getTime() - latelyTime;
			int d = (int) (LATELY_DAYS_COUNT - duration / MILLISECONDS_ONE_DAY - 1);
			return CHINESE_NUMBERS[d] +DAYS_BEFORE ;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		String displayDate = String.format("%s %s", DayFormatter.format(dt),
				WEEK_DAYS[w]);
		return displayDate;
	}
	
	public static String formatTime(Date dt){
		return TimeDateFormatter.format(dt);
	}

	public static String formatDateWithWeekDay(Date dt) {
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
			return CHINESE_NUMBERS[d] + DAYS_BEFORE;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		String displayDate = String.format("%s %s", DayFormatter.format(dt),
				WEEK_DAYS[w]);
		return displayDate;
	}
	
	public static String formatDateTime(Date dt){
		if(isSameDay(dt.getTime(), System.currentTimeMillis())){
			return TodayDateFormatter.format(dt);
		}
		return DateFormatter.format(dt);
	}

	public static boolean isSameDay(Date dt1, Date dt2) {
		return isSameDay(dt1.getTime(), dt2.getTime());
	}

	public static boolean isSameDay(long t1, long t2) {
		return (t1 / MILLISECONDS_ONE_DAY) == (t2 / MILLISECONDS_ONE_DAY);
	}

	public static boolean isSameMinute(Date dt1, Date dt2) {
		return isSameMinute(dt1.getTime(), dt2.getTime());
	}

	public static boolean isSameMinute(long t1, long t2) {
		return (t1 / MILLISECONDS_ONE_MINUTE) == (t2 / MILLISECONDS_ONE_MINUTE);
	}
	
	public static String formatDuration(long duration){
		long hour=duration/SECONDS_ONE_HOUR;
		long minute=(duration%SECONDS_ONE_HOUR)/SECONDS_ONE_MINUTE;
		long second=duration%SECONDS_ONE_MINUTE;
		StringBuilder sb=new StringBuilder();
		if(hour>0){
			sb.append(hour).append(HOUR).append(minute).append(MINUTE);
		}else if(minute>0){
			sb.append(minute).append(MINUTE);
		}
		sb.append(second).append(SECOND);
		return sb.toString();
	}
}
