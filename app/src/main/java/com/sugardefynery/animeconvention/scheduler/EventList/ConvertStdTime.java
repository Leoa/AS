package com.sugardefynery.animeconvention.scheduler.EventList;

import java.text.SimpleDateFormat;

public class ConvertStdTime {

	private int hour;
	private int minute;
	private int second;
	private String ampmSwitch;
	String AmorPm;
	int x;
	int returnThis;
	int min;

	
	//checks data if user puts in a time
	public String setTime(int h, int m, int s) {

		hour = ((h >= 0 && h < 24) ? h : 0);
		if (h == 0) {
			hour = 12;
		}
		 hour=(h / 100) * 60 + (h % 100);
		minute = ((m >= 0 && m < 60) ? m : 0);
		second = ((s >= 0 && s < 60) ? s : 0);
		ampmSwitch = (h >= 12) ? "pm" : "am";

		return String.format("%02d:%02d:%02d %s", hour, minute, second,
				ampmSwitch);

	}

	public String toMilitaryString(String time) {

		int index = time.indexOf(':');
		String defaultStartHour = time.substring(0, index);
		String defaultStartMin = time.substring(index + 1, index + 3);
		AmorPm = time.substring(time.length() - 2);
		x = Integer.parseInt(defaultStartHour);
		String am = "am";

		if (AmorPm.compareToIgnoreCase(am) == 0) {

			if (x == 12 || x == 24) {
				x = 0;
			}
			hour = x;

		}

		// switch() JDK compliance at 1.7. Android runs on 1.5 or 1.6.
		if (AmorPm.compareToIgnoreCase(am) != 0) {

			if (x == 12) {
				x = 12;
			}
			if (x == 1) {
				x = 13;
			}
			if (x == 2) {
				x = 14;
			}
			if (x == 3) {
				x = 15;
			}
			if (x == 4) {
				x = 16;
			}
			if (x == 5) {
				x = 17;
			}
			if (x == 6) {
				x = 18;
			}
			if (x == 7) {
				x = 19;
			}
			if (x == 8) {
				x = 20;
			}
			if (x == 9) {
				x = 21;
			}
			if (x == 10) {
				x = 22;
			}
			if (x == 11) {
				x = 23;
			}
			hour = x;

		}

		String timeMin = defaultStartMin;
		String timeSec = "00";
		return String.format("%02d:%s:%s", hour, timeMin, timeSec);

	}

	public String toStandard(int mHour, int mMinute) {
		
		int modulus=mHour%12;
		System.out.println( "hour is "+mHour +"modous " +modulus);
		return String.format("%d:%02d %s", ((mHour == 0 || mHour == 12) ? 12 : mHour % 12), mMinute,  (mHour < 12 ? "am" : "pm"));
	}

	public int toMilitary(String h) {

		// if the minutes changed how do campare them?

		int index = h.indexOf(':');
		String defaultStartHour = h.substring(0, index);
		String defaultStartMin = h.substring(index + 1, index + 3);
		min = Integer.parseInt(defaultStartMin);
		AmorPm = h.substring(h.length() - 2);
		x = Integer.parseInt(defaultStartHour);
		String am = "am";

		if (AmorPm.compareToIgnoreCase(am) == 0) {

			if (x == 12 || x == 24) {
				x = 0;
			}
			returnThis = x;

		}

		// switch() JDK compliance at 1.7. Android runs on 1.5 or 1.6.
		if (AmorPm.compareToIgnoreCase(am) != 0) {

			if (x == 12) {
				x = 12;
			}
			if (x == 1) {
				x = 13;
			}
			if (x == 2) {
				x = 14;
			}
			if (x == 3) {
				x = 15;
			}
			if (x == 4) {
				x = 16;
			}
			if (x == 5) {
				x = 17;
			}
			if (x == 6) {
				x = 18;
			}
			if (x == 7) {
				x = 19;
			}
			if (x == 8) {
				x = 20;
			}
			if (x == 9) {
				x = 21;
			}
			if (x == 10) {
				x = 22;
			}
			if (x == 11) {
				x = 23;
			}
			returnThis = x;

		}

		return returnThis;

	}

	public int getMin() {

		return min;
	}
}