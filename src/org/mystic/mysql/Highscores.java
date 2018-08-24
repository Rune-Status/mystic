package org.mystic.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;

public class Highscores implements Runnable {

	public static final String HOST = "198.252.108.22";

	public static final String USER = "mysticps_hs";

	public static final String DATABASE = "mysticps_highscores";

	public static final String PASS = "+6I5p7?oCe=+";

	public static final String TABLE = "hs_users";

	private final Player player;

	private Connection conn;

	private Statement stmt;

	public Highscores(Player player) {
		this.player = player;
	}

	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getMode() {
		if (player.getRights().equals(Rights.HARDCORE_IRON_MAN)) {
			return 3;
		} else if (player.getRights().equals(Rights.ULTIMATE_IRON_MAN)) {
			return 2;
		} else if (player.getRights().equals(Rights.IRON_MAN)) {
			return 1;
		} else {
			return 0;
		}
	}

	public int getDisplay() {
		if (player.getRights().equals(Rights.HARDCORE_IRON_MAN)) {
			return 3;
		} else if (player.getRights().equals(Rights.ULTIMATE_IRON_MAN)) {
			return 2;
		} else if (player.getRights().equals(Rights.IRON_MAN)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}
			if (!player.getRights().equals(Rights.OWNER) && !player.getRights().equals(Rights.ADMINISTRATOR)) {
				String name = player.getUsername();
				PreparedStatement stmt1 = prepare("DELETE FROM " + TABLE + " WHERE username=?");
				stmt1.setString(1, name);
				stmt1.execute();
				PreparedStatement stmt2 = prepare(generateQuery());
				stmt2.setString(1, player.getUsername());
				stmt2.setInt(2, getDisplay());
				stmt2.setInt(3, getMode());
				stmt2.setLong(4, player.getSkill().getTotalExperience());
				for (int i = 0; i < 25; i++) {
					stmt2.setInt(5 + i, (int) player.getSkill().getExperience()[i]);
				}
				stmt2.execute();
			}
			destroy();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}

	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + TABLE + " (");
		sb.append("username, ");
		sb.append("rights, ");
		sb.append("mode, ");
		sb.append("overall_xp, ");
		sb.append("attack_xp, ");
		sb.append("defence_xp, ");
		sb.append("strength_xp, ");
		sb.append("constitution_xp, ");
		sb.append("ranged_xp, ");
		sb.append("prayer_xp, ");
		sb.append("magic_xp, ");
		sb.append("cooking_xp, ");
		sb.append("woodcutting_xp, ");
		sb.append("fletching_xp, ");
		sb.append("fishing_xp, ");
		sb.append("firemaking_xp, ");
		sb.append("crafting_xp, ");
		sb.append("smithing_xp, ");
		sb.append("mining_xp, ");
		sb.append("herblore_xp, ");
		sb.append("agility_xp, ");
		sb.append("thieving_xp, ");
		sb.append("slayer_xp, ");
		sb.append("farming_xp, ");
		sb.append("runecrafting_xp, ");
		sb.append("hunter_xp, ");
		sb.append("construction_xp, ");
		sb.append("summoning_xp, ");
		sb.append("dungeoneering_xp) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		return sb.toString();
	}

}