package org.mystic.utility;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jboss.netty.buffer.ChannelBuffer;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.out.OutgoingPacket;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendString;

/**
 * A collection of miscellaneous utility methods and constants.
 * 
 * @author blakeman8192
 */
public class Misc {

	public static class Stopwatch {

		private long time = System.currentTimeMillis();

		public Stopwatch headStart(long startAt) {
			time = System.currentTimeMillis() - startAt;
			return this;
		}

		public Stopwatch reset(long i) {
			time = i;
			return this;
		}

		public Stopwatch reset() {
			time = System.currentTimeMillis();
			return this;
		}

		public long elapsed() {
			return System.currentTimeMillis() - time;
		}

		public boolean elapsed(long time) {
			return elapsed() >= time;
		}

		public long getTime() {
			return time;
		}

		public Stopwatch() {
			time = 0;
		}
	}

	/**
	 * gets formatted time
	 * 
	 * @param secs
	 * @return
	 */
	public static String getFormattedTime(int secs) {
		if (secs < 60)
			return "00:" + secs + "";
		else {
			int mins = secs / 60;
			int remainderSecs = secs - (mins * 60);
			if (mins < 60) {
				return (mins < 10 ? "0" : "") + mins + ":" + (remainderSecs < 10 ? "0" : "") + remainderSecs + "";
			} else {
				int hours = mins / 60;
				int remainderMins = mins - (hours * 60);
				return (hours < 10 ? "0" : "") + hours + "h " + (remainderMins < 10 ? "0" : "") + remainderMins + "m "
						+ (remainderSecs < 10 ? "0" : "") + remainderSecs + "s";
			}
		}
	}

	public static String anOrA(String s) {
		s = s.toLowerCase();
		if (s.equalsIgnoreCase("anchovies") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese")
				|| s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice")
				|| s.equalsIgnoreCase("steel nails") || s.equalsIgnoreCase("snape grass") || s.equalsIgnoreCase("coal"))
			return "some";
		if (s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u"))
			return "an";
		return "a";
	}

	/**
	 * Capitalizes letters
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()));
	}

	public static String formatTime(long milliseconds) {
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		return hours + "hr " + minutes + "min" + " " + seconds + "sec";
	}

	/**
	 * A simple logging utility that prefixes all messages with a timestamp.
	 * 
	 * @author blakeman8192
	 */
	public static class TimestampLogger extends PrintStream {

		private BufferedWriter writer;
		private DateFormat df = new SimpleDateFormat();

		/**
		 * The OutputStream to log to.
		 * 
		 * @param out
		 */
		public TimestampLogger(OutputStream out, String file) throws IOException {
			super(out);
			writer = new BufferedWriter(new FileWriter(file, true));
		}

		/**
		 * Logs the message to the log file.
		 * 
		 * @param msg
		 *            the message
		 */
		private void log(String msg) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void println(String msg) {
			msg = "[" + df.format(new Date()) + "]: " + msg;
			super.println(msg);
			log(msg);
		}

	}

	public static final Random random = new Random();

	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	public static final int LOGIN_RESPONSE_OK = 2;
	public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
	public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
	public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
	public static final int LOGIN_RESPONSE_UPDATED = 6;
	public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
	public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
	public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
	public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
	public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
	public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
	public static final int LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN = 13;
	public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
	public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
	public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;
	public static final int EQUIPMENT_SLOT_HEAD = 0;
	public static final int EQUIPMENT_SLOT_CAPE = 1;
	public static final int EQUIPMENT_SLOT_AMULET = 2;
	public static final int EQUIPMENT_SLOT_WEAPON = 3;
	public static final int EQUIPMENT_SLOT_CHEST = 4;
	public static final int EQUIPMENT_SLOT_SHIELD = 5;
	public static final int LEGS_SLOT = 7;
	public static final int EQUIPMENT_SLOT_HANDS = 9;
	public static final int EQUIPMENT_SLOT_FEET = 10;
	public static final int EQUIPMENT_SLOT_RING = 12;
	public static final int EQUIPMENT_SLOT_ARROWS = 13;
	public static final int APPEARANCE_SLOT_CHEST = 1;
	public static final int APPEARANCE_SLOT_ARMS = 2;
	public static final int APPEARANCE_SLOT_LEGS = 4;
	public static final int APPEARANCE_SLOT_HEAD = 0;
	public static final int APPEARANCE_SLOT_HANDS = 3;
	public static final int APPEARANCE_SLOT_FEET = 5;
	public static final int APPEARANCE_SLOT_BEARD = 6;
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;
	private static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c',
			'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']' };
	private static char decodeBuf[] = new char[4096];

	public static final char playerNameXlateTable[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };

	/**
	 * Lengths for the various packets.
	 */
	public static final int packetLengths[] = {

			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 4, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, -1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 4, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250

	};

	public static String capitalizeFirstLetter(final String string) {

		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static boolean containsOnlyNumbers(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Random instance, used to generate pseudo-random primitive types.
	 */
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	/**
	 * Gets a random number
	 */
	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	/**
	 * Picks a random element out of any array type.
	 * 
	 * @param collection
	 *            the collection to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(Collection<T> collection) {
		return new ArrayList<T>(collection).get((int) (RANDOM.nextDouble() * collection.size()));
	}

	/**
	 * Picks a random element out of any array type.
	 * 
	 * @param array
	 *            the array to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	/**
	 * Picks a random element out of any list type.
	 * 
	 * @param list
	 *            the list to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(List<T> list) {
		return list.get((int) (RANDOM.nextDouble() * list.size()));
	}

	public static String convertTime(String input) {
		try {
			input = input.toLowerCase();

			if (input.contains("am")) {
				return input.replace("am", "").trim();
			} else if (input.contains("pm")) {
				if (input.contains("12")) {
					return "12";
				} else {
					int t = Integer.parseInt(input.substring(0, input.indexOf("p")).trim());

					return "" + (t + 12);
				}
			} else {
				int time = Integer.parseInt(input);

				if (time > 11 && time != 24) {
					if (time > 12) {
						return (time - 12) + " pm";
					} else {
						return "12 pm";
					}
				} else if (time == 24) {
					return "12 am";
				} else {
					return time + " am";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the delta coordinates. Note that the returned Location is not an
	 * actual location, instead it's values represent the delta values between the
	 * two arguments.
	 * 
	 * @param a
	 *            the first location
	 * @param b
	 *            the second location
	 * @return the delta coordinates contained within a location
	 */
	public static Location delta(Location a, Location b) {
		return new Location(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/**
	 * Calculates the direction between the two coordinates.
	 * 
	 * @param dx
	 *            the first coordinate
	 * @param dy
	 *            the second coordinate
	 * @return the direction
	 */
	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0) {
				return 5;
			} else if (dy > 0) {
				return 0;
			} else {
				return 3;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				return 7;
			} else if (dy > 0) {
				return 2;
			} else {
				return 4;
			}
		} else {
			if (dy < 0) {
				return 6;
			} else if (dy > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public static String displayCoin(int amount) {
		String string = "";
		int thousand = amount / 1000;
		int million = amount / 1000000;
		int billion = amount / 1000000000;
		if (amount > 0 && amount <= 998) {
			string = "GP";
		} else if (amount > 999 && amount <= 999999) {
			string = Integer.toString(thousand) + "K";
		} else if (amount > 999999 && amount <= 999999999) {
			string = Integer.toString(million) + "M";
		} else if (amount > 999999999) {
			string = Integer.toString(billion) + "B";
		} else {
			return "x" + amount;
		}
		return string;
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatBillionCoins(int[] amount) {
		int num = 0;
		int rem = 0;

		for (int i : amount) {
			num += i / 1000;
			rem += i % 1000;
		}

		if (rem >= 1000) {
			num += rem / 1000;
		}

		int bill = num / 1000000;
		num -= bill * 1000000;

		int mill = num / 1000;

		String z = "";
		if (mill < 10) {
			z = "00";
		} else if (mill < 100) {
			z = "0";
		}

		return bill + "." + z + mill + "B";
	}

	public static String formatCoins(int amount) {
		if (amount >= 10000000) {
			return amount / 1000000 + "M";
		} else if (amount >= 100000) {
			return amount / 1000 + "K";
		} else {
			return amount + "x";
		}
	}

	public static String formatCoins(long amount) {
		if (amount >= 10000000) {
			return amount / 1000000 + "M";
		} else if (amount >= 100000) {
			return amount / 1000 + "K";
		} else {
			return amount + "x";
		}
	}

	public static String formatCoins2(int amount) {
		if (amount > 999) {
			return amount / 1000 + "K";
		} else {
			return amount + "gp";
		}
	}

	public static String formatPlayerName(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}

	public static String getAOrAn(String nextWord) {
		String s = "a";
		char c = nextWord.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			s = "an";
		}
		return s;
	}

	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month) {
				days += daysOfTheMonth[i];
			}
		}
		return days;
	}

	public static int getElapsed(int day, int year) {
		if (year < 2013) {
			return 0;
		}

		int elapsed = 0;
		int currentYear = Misc.getYear();
		int currentDay = Misc.getDayOfYear();

		if (currentYear == year) {
			elapsed = currentDay - day;
		} else {
			elapsed = currentDay;

			for (int i = 1; i < 5; i++) {
				if (currentYear - i == year) {
					elapsed += 365 - day;
					break;
				} else {
					elapsed += 365;
				}
			}
		}

		return elapsed;
	}

	/**
	 * Returns the distance between 2 points
	 * 
	 * @param a
	 *            The first location
	 * @param b
	 *            The second location
	 * @return
	 */
	public static double getExactDistance(Location a, Location b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	public static int getManhattanDistance(int x, int y, int x2, int y2) {
		return Math.abs(x - x2) + Math.abs(y - y2);
	}

	/**
	 * Gets the distance between 2 points
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getManhattanDistance(Location a, Location b) {
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	public static int getMinutesElapsed(int minute, int hour, int day, int year) {
		Calendar i = Calendar.getInstance();

		if (i.get(1) == year) {
			if (i.get(6) == day) {
				if (hour == i.get(11)) {
					return i.get(12) - minute;
				}
				return (i.get(11) - hour) * 60 + (59 - i.get(12));
			}

			int ela = (i.get(6) - day) * 24 * 60 * 60;
			return ela > 2147483647 ? 2147483647 : ela;
		}

		int ela = getElapsed(day, year) * 24 * 60 * 60;

		return ela > 2147483647 ? 2147483647 : ela;
	}

	public static int getRandom(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	/**
	 * Reads a RuneScape string from a buffer.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The string.
	 */
	public static String getRS2String(final ChannelBuffer buf) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.readable() && (b = buf.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	public static final <E> E getWhereNotEqualTo(List<E> list, E e) {
		List<E> sub = new ArrayList<E>();

		for (Iterator<E> i = list.iterator(); i.hasNext();) {
			E k = i.next();
			if (!k.equals(e)) {
				sub.add(k);
			}
		}

		return sub.get(randomNumber(sub.size()));
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (int i = 0; i < data.length; i++) {
			int num = (data[i] & 0xFF) * n;
			value += num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	public static String insertCommas(String str) {
		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}

	public static boolean isExpired(int day, int year, int length) {
		if (getElapsed(day, year) >= length) {
			return true;
		}

		return false;
	}

	public static boolean isWeekend() {
		int day = Calendar.getInstance().get(7);
		return (day == 1) || (day == 6) || (day == 7);
	}

	public static String longToPlayerName2(long l) {
		int i = 0;
		char ac[] = new char[99];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	/**
	 * Converts the username to a long value.
	 * 
	 * @param s
	 *            the username
	 * @return the long value
	 */
	public static long nameToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static boolean needsAnA(String word) {
		int length = word.length();
		return !word.substring(length - 1, length).equals("s");
	}

	public static void openBlankQuestDialogue(Player player) {
		player.getClient().queueOutgoingPacket(new SendString("", 8144));
		int c = 0;
		while (c <= 50) {
			player.getClient().queueOutgoingPacket(new SendString("", 8145 + c));
			c++;
		}
		c = 0;
		while (c <= 49) {
			player.getClient().queueOutgoingPacket(new SendString("", 12174 + c));
			c++;
		}
		player.getClient().queueOutgoingPacket(new SendInterface(8134));
	}

	/**
	 * Returns a random integer with min as the inclusive lower bound and max as the
	 * exclusive upper bound.
	 * 
	 * @param min
	 *            The inclusive lower bound.
	 * @param max
	 *            The exclusive upper bound.
	 * @return Random integer min <= n < max.
	 */
	public static int random(int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
	}

	public static int randomMinusOne(int range) {
		int number = (int) (Math.random() * range);
		return number < 0 ? 0 : number;
	}

	public static int randomNumber(int length) {
		return (int) (java.lang.Math.random() * length);
	}

	public static void sendPacketToPlayers(OutgoingPacket packet, List<Player> players) {
		for (Player i : players) {
			i.getClient().queueOutgoingPacket(packet);
		}
	}

	/**
	 * Determines if a word starts with a vowel, thus the prefix with be 'an'.
	 * Joshua Barry <Arsenic>
	 * 
	 * @param word
	 *            The word to check.
	 * @return whether the word starts with a vowel or not.
	 */
	public static boolean startsWithVowel(String word) {
		if (word != null) {
			word = word.toLowerCase();
			return (word.charAt(0) == 'a' || word.charAt(0) == 'e' || word.charAt(0) == 'i' || word.charAt(0) == 'o'
					|| word.charAt(0) == 'u');

		}
		return false;
	}

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13) {
					decodeBuf[idx++] = xlateTable[val];
				} else {
					highNibble = val;
				}
			} else {
				decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}

		return new String(decodeBuf, 0, idx);
	}
}
