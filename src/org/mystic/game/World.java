package org.mystic.game;

import java.util.List;

import org.mystic.game.model.content.combat.CombatConstants;
import org.mystic.game.model.content.minigames.fightpits.FightPits;
import org.mystic.game.model.content.minigames.pestcontrol.PestControl;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcUpdateFlags;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.PlayerUpdateFlags;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.outgoing.SendGameUpdateTimer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendNPCUpdate;
import org.mystic.game.model.networking.outgoing.SendPlayerUpdate;
import org.mystic.game.model.networking.outgoing.SendProjectile;
import org.mystic.game.model.networking.outgoing.SendStillGraphic;
import org.mystic.utility.Misc;
import org.mystic.utility.MobUpdateList;

/**
 * Handles the in-game world
 * 
 * @author Michael Sasse
 * 
 */
public class World {

	/**
	 * The maximum amount of players that can be processed
	 */
	public static final int MAX_PLAYERS = 500;

	/**
	 * The maximum amount of mobs available in the in-game world
	 */
	public static final int MAX_MOBS = 5000;

	/**
	 * A list of players registered into the game world
	 */
	private static final Player[] players = new Player[MAX_PLAYERS];

	/**
	 * A list of mobs registered into the game world
	 */
	private static final Npc[] mobs = new Npc[MAX_MOBS];

	/**
	 * The servers cycles?
	 */
	private static long cycles = 0L;

	/**
	 * A list of updated mobs
	 */
	private static MobUpdateList mobUpdateList = new MobUpdateList();

	/**
	 * The current server update timer
	 */
	private static int updateTimer = -1;

	public static boolean inUpdate() {
		return updateTimer > 0;
	}

	/**
	 * The server is being updated
	 */
	private static boolean updating = false;

	/**
	 * is the tick ignored
	 */
	private static boolean ignoreTick = false;

	/**
	 * Gets the active amount of players online
	 * 
	 * @return
	 */
	public static int getActivePlayers() {
		int count = 0;
		for (Player p : players) {
			if (p != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the cycles
	 * 
	 * @return
	 */
	public static long getCycles() {
		return cycles;
	}

	/**
	 * Gets the list of in-game mobs
	 * 
	 * @return
	 */
	public static Npc[] getNpcs() {
		return mobs;
	}

	/**
	 * Gets a player by their name as a long
	 * 
	 * @param n
	 *            The players username as a long
	 * @return
	 */
	public static Player getPlayerByName(long n) {
		for (Player p : players) {
			if ((p != null) && (p.isActive()) && (p.getUsernameToLong() == n)) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Gets a player by their username
	 * 
	 * @param username
	 *            The players username
	 * @return
	 */
	public static Player getPlayerByName(String username) {
		if (username == null) {
			return null;
		}
		long n = Misc.nameToLong(username.toLowerCase());
		for (Player p : players) {
			if ((p != null) && (p.isActive()) && (p.getUsernameToLong() == n)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets the list of players
	 * 
	 * @return
	 */
	public static Player[] getPlayers() {
		return players;
	}

	/**
	 * Initiates an in-game update
	 */
	public static void initUpdate(int timer) {
		updateTimer = timer;
		if (updateTimer < 0) {
			updateTimer = 100;
		}
		for (Player p : players) {
			if (p != null) {
				p.getClient().queueOutgoingPacket(new SendGameUpdateTimer(updateTimer));
				p.send(new SendMessage("@blu@@cr6@  Notice: Mystic will be undergoing a system update  @cr6@"));
			}
		}
	}

	/**
	 * Is the tick ignored
	 * 
	 * @return
	 */
	public static boolean isIgnoreTick() {
		return ignoreTick;
	}

	/**
	 * Checks if a mobs index is within range
	 * 
	 * @param mobIndex
	 * @return
	 */
	public static boolean isMobWithinRange(int mobIndex) {
		return (mobIndex > -1) && (mobIndex < mobs.length);
	}

	/**
	 * Checks if a player is within range to be registered
	 * 
	 * @param playerIndex
	 *            The index of the player
	 * @return
	 */
	public static boolean isPlayerWithinRange(int playerIndex) {
		return (playerIndex > -1) && (playerIndex < players.length);
	}

	/**
	 * Is the server being updated
	 * 
	 * @return
	 */
	public static boolean isUpdating() {
		return updating;
	}

	/**
	 * The amount of npcs registered into the game world
	 * 
	 * @return
	 */
	public static int npcAmount() {
		int amount = 0;
		for (int i = 1; i < mobs.length; i++) {
			if (mobs[i] != null) {
				amount++;
			}
		}
		return amount;
	}

	/**
	 * Handles processing the main game world
	 */
	public static void process() {
		PlayerUpdateFlags[] player_flags = new PlayerUpdateFlags[players.length];
		NpcUpdateFlags[] npc_flags = new NpcUpdateFlags[mobs.length];
		try {
			FightPits.tick();
			PestControl.tick();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 1; i < MAX_PLAYERS; i++) {
			Player player = players[i];
			try {
				if (player != null) {
					if (!player.isActive()) {
						if (player.getClient().getStage() == Client.Stages.LOGGED_IN) {
							player.setActive(true);
							player.start();

							player.getClient().resetLastPacketReceived();
						} else if (getCycles() - player.getClient().getLastPacketTime() > 30) {
							player.logout(true);
						}
					}
					player.getClient().processIncomingPackets();
					player.process();
					player.getClient().reset();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				if (player != null) {
					player.logout(true);
				}
			}
		}
		for (int i = 0; i < mobs.length; i++) {
			Npc mob = mobs[i];
			if (mob != null) {
				try {
					mob.process();
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}
		for (int i = 1; i < MAX_PLAYERS; i++) {
			Player player = players[i];
			if ((player == null) || (!player.isActive()))
				player_flags[i] = null;
			else {
				try {
					player.getMovementHandler().process();
					player_flags[i] = new PlayerUpdateFlags(player);
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 0; i < mobs.length; i++) {
			Npc mob = mobs[i];
			if (mob != null) {
				try {
					mob.processMovement();
					npc_flags[mob.getIndex()] = new NpcUpdateFlags(mob);
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}
		for (int i = 1; i < MAX_PLAYERS; i++) {
			Player player = players[i];
			if ((player != null) && (player_flags[i] != null) && (player.isActive())) {
				try {
					player.getClient().queueOutgoingPacket(new SendPlayerUpdate(player_flags));
					player.getClient().queueOutgoingPacket(new SendNPCUpdate(npc_flags, player_flags[i]));
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 1; i < MAX_PLAYERS; i++) {
			Player player = players[i];
			if ((player != null) && (player.isActive())) {
				try {
					player.reset();
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 0; i < mobs.length; i++) {
			Npc mob = mobs[i];
			if (mob != null) {
				try {
					mob.reset();
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}
		if ((updateTimer > -1) && ((World.updateTimer = (short) (updateTimer - 1)) == 0)) {
			update();
		}
		if (ignoreTick) {
			ignoreTick = false;
		}
		cycles += 1L;
	}

	public static int register(Npc mob) {
		if (mob.isRegistered()) {
			return -1;
		}
		for (int i = 1; i < mobs.length; i++) {
			if (mobs[i] == null) {
				mobs[i] = mob;
				mob.setIndex(i);
				mob.setRegistered(true);
				return i;
			}
		}
		return -1;
	}

	/**
	 * Registers a player into the in-game world
	 * 
	 * @param player
	 *            The player to register into the game world
	 * @return
	 */
	public static int register(Player player) {
		int[] ids = new int[players.length];
		int c = 0;
		for (int i = 1; i < players.length; i++) {
			if (players[i] == null) {
				ids[c] = i;
				c++;
			}
		}
		if (c == 0) {
			return -1;
		}
		int index = ids[Misc.randomNumber(c)];
		players[index] = player;
		player.setIndex(index);
		for (int k = 1; k < players.length; k++) {
			if ((players[k] != null) && (players[k].isActive())) {
				players[k].getPrivateMessaging().updateOnlineStatus(player, true);
			}
		}
		if (updateTimer > -1) {
			player.getClient().queueOutgoingPacket(new SendGameUpdateTimer(updateTimer));
		}
		return c;
	}

	public static void remove(List<Npc> local) {
		local.clear();
	}

	/**
	 * Resets an in-game update
	 */
	public static void resetUpdate() {
		updateTimer = -1;
		for (Player p : players) {
			if (p != null) {
				p.getClient().queueOutgoingPacket(new SendGameUpdateTimer(0));
			}
		}
	}

	/**
	 * Sends a global message to all players online
	 * 
	 * @param message
	 *            The message to send to all players
	 * @param format
	 *            Should the message beformatted
	 */
	public static void sendGlobalMessage(String message, boolean format) {
		message = (format ? "<col=255>" : "") + message + (format ? "</col>" : "");
		for (Player p : players) {
			if ((p != null) && (p.isActive())) {
				p.getClient().queueOutgoingPacket(new SendMessage(message));
			}
		}
	}

	/**
	 * Sends a global message to all players online
	 * 
	 * @param message
	 *            The message to send to all players
	 * @param format
	 *            Should the message beformatted
	 */
	public static void sendGlobalStaffMessage(String message, boolean format) {
		message = (format ? "<col=255>" : "") + message + (format ? "</col>" : "");
		for (Player p : players) {
			if ((p != null) && (p.isActive() && PlayerConstants.isOwner(p))) {
				p.getClient().queueOutgoingPacket(new SendMessage(message));
			}
		}
	}

	public static void sendProjectile(Projectile p, Entity e1, Entity e2) {
		int lockon = e2.isNpc() ? e2.getIndex() + 1 : -e2.getIndex() - 1;
		byte offsetX = (byte) ((e1.getLocation().getY() - e2.getLocation().getY()) * -1);
		byte offsetY = (byte) ((e1.getLocation().getX() - e2.getLocation().getX()) * -1);
		sendProjectile(p, CombatConstants.getOffsetProjectileLocation(e1), lockon, offsetX, offsetY);
	}

	/**
	 * Sends a projectile
	 * 
	 * @param projectile
	 *            The id of the graphic
	 * @param pLocation
	 *            The location to send the graphic too
	 * @param lockon
	 *            The lockon index
	 * @param offsetX
	 *            The x offset of the projectile
	 * @param offsetY
	 *            The y offset of the projectile
	 */
	public static void sendProjectile(Projectile projectile, Location pLocation, int lockon, byte offsetX,
			byte offsetY) {
		for (Player player : players) {
			if (player != null) {
				if (pLocation.isViewableFrom(player.getLocation())) {
					player.getClient().queueOutgoingPacket(
							new SendProjectile(player, projectile, pLocation, lockon, offsetX, offsetY));
				}
			}
		}
	}

	/**
	 * Sets a still graphic to a location
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param location
	 *            The location of the graphic
	 */
	public static void sendStillGraphic(int id, int delay, Location location) {
		for (Player player : players) {
			if ((player != null) && (location.isViewableFrom(player.getLocation()))) {
				player.getClient().queueOutgoingPacket(new SendStillGraphic(id, location, delay));
			}
		}
	}

	/**
	 * Sets the tick to be ignored
	 * 
	 * @param ignore
	 *            Should the tick be ignored
	 */
	public static void setIgnoreTick(boolean ignore) {
		ignoreTick = ignore;
	}

	public static void unregister(Npc mob) {
		if (mob.getIndex() == -1 || !mob.isRegistered()) {
			return;
		}
		mobs[mob.getIndex()] = null;
		mobUpdateList.toRemoval(mob);
		mob.setRegistered(false);
	}

	/**
	 * Unregisters a player from the game world
	 * 
	 * @param player
	 *            The player to unregister into the game world
	 */
	public static void unregister(Player player) {
		if ((player.getIndex() == -1) || (players[player.getIndex()] == null)) {
			return;
		}
		players[player.getIndex()] = null;
		for (int i = 0; i < players.length; i++) {
			if ((players[i] != null) && (players[i].isActive())) {
				players[i].getPrivateMessaging().updateOnlineStatus(player, false);
			}
		}
	}

	/**
	 * Updates the server by disconnecting all players
	 */
	public static void update() {
		updating = true;
		for (Player p : players) {
			if (p != null) {
				p.logout(true);
			}
		}
	}
	
}