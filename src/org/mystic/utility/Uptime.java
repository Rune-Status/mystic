package org.mystic.utility;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Uptime {

	public static String get() {
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return "" + df.format(new Date(mx.getUptime()));
	}

}
