package org.mystic.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mystic.game.World;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class Donation implements Runnable {

	public static final String HOST = "198.252.108.22";

	public static final String USER = "mysticps_store";

	public static final String PASS = "nCCH(r4H6Ir-";

	public static final String DATABASE = "mysticps_shop";

	private final Player player;

	private Connection conn;

	private Statement stmt;

	/**
	 * The constructor
	 * 
	 * @param player
	 */
	public Donation(Player player) {
		this.player = player;
	}

	public void sendMessages(Player player) {
		DialogueManager.sendStatement(player, "Your donation was successful and your rewards have been sent!",
				"If your inventory was full your rewards will", "have been sent to your bank account.",
				"Thanks for showing your support!");
		World.sendGlobalStaffMessage("@dre@(Staff Only) -> Player: " + player.getUsername()
				+ " has successfuly made and claimed a donation!", true);
		PlayerLogs.log(player.getUsername(), "claimed a successful donation and received their items.");
	}

	@SuppressWarnings("unused")
	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				player.send(new SendMessage("@dre@Unfortunately the store appears to be offline right now.."));
				return;
			}
			final String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery(
					"SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");
			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");
				switch (item_number) {

				// membership bond
				case 10:
					sendMessages(player);
					player.getInventory().add(new Item(20086, quantity));
					break;

				// 100 point scroll
				case 11:
					sendMessages(player);
					player.getInventory().add(new Item(6758, quantity));
					break;

				// 500 point scroll
				case 12:
					sendMessages(player);
					player.getInventory().add(new Item(607, quantity));
					break;

				// 1000 point scroll
				case 13:
					sendMessages(player);
					player.getInventory().add(new Item(608, quantity));
					break;

				}
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param host
	 *            the host ip address or url
	 * @param database
	 *            the name of the database
	 * @param user
	 *            the user attached to the database
	 * @param pass
	 *            the users password
	 * @return true if connected
	 */
	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Disconnects from the MySQL server and destroy the connection and statement
	 * instances
	 */
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

	/**
	 * Executes an update query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeUpdate}
	 */
	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * Executres a query on the database
	 * 
	 * @param query
	 * @see {@link Statement#executeQuery(String)}
	 * @return the results, never null
	 */
	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}