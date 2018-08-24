package org.mystic.game.model.player.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mystic.utility.Misc;

public class PlayerLogs {

	private static final String FILE_PATH = "./data/logs/players/";

	/**
	 * Fetches system time and formats it appropriately
	 * 
	 * @return Formatted time
	 */
	private static String getTime() {
		Date getDate = new Date();
		String timeFormat = "M/d/yy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return "[" + sdf.format(getDate) + "]\t";
	}

	public static void multiLog(String param, String pattern, String[]... data) {
		if (data == null || data.length == 0 || data[0].length == 0) {
			return;
		}
		param = Misc.formatPlayerName(param.toLowerCase().trim());
		File file = new File(FILE_PATH + param + ".txt");
		if (!file.exists()) {
			try {
				if (!file.createNewFile()) {
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("---" + getTime() + "---");
			writer.newLine();
			for (int index = 0; index < data.length; index++) {
				writer.write(String.format(pattern, (Object[]) data[index]));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes formatted string to text file
	 * 
	 * @param file
	 *            - File to write data to
	 * @param ORE_DATA
	 *            - Data to written
	 * @throws IOException
	 */
	public static void log(String file, String writable) {
		try {
			FileWriter fw = new FileWriter(FILE_PATH + file + ".txt", true);
			if (fw != null) {
				fw.write(getTime() + writable + "\t");
				fw.write(System.lineSeparator());
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}