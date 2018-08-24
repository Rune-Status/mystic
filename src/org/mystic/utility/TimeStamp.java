package org.mystic.utility;

import java.util.Calendar;

public class TimeStamp {

	private final int minute;
	private final int hour;
	private final int day;
	private final int year;

	public TimeStamp() {
		this.minute = Calendar.getInstance().get(12);
		this.hour = Calendar.getInstance().get(10);
		this.day = Calendar.getInstance().get(6);
		this.year = Calendar.getInstance().get(1);
	}

	public int getHoursElapsed() {
		return Misc.getMinutesElapsed(minute, hour, day, year) / 60;
	}

	public int getMinutesElapsed() {
		return Misc.getMinutesElapsed(minute, hour, day, year);
	}
}
