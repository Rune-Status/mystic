package org.mystic.game.model.content.minigames.godwars;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.minigames.godwars.GodWarsData.Allegiance;
import org.mystic.game.model.content.minigames.godwars.GodWarsData.GodWarsNpc;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendSound;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.WalkThroughDoorTask;

public class GodWars {

	/**
	 * Points required to enter the room
	 */
	public static final int POINTS_TO_ENTER = 10;

	/**
	 * Godwars Key
	 */
	public static final String GWD_ALTAR_KEY = "GWD_ALTAR_KEY";

	/**
	 * Ecumencial Key Identification
	 */
	public static final int ECUMENICAL_KEY = 11040;

	/**
	 * Handles clicing object for Godwars
	 * 
	 * @param player
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static boolean clickObject(Player player, int id, int x, int y, int z) {
		switch (id) {
		/**
		 * Armadyl room
		 */
		case 26502:
			if (player.getY() > 5294) {
				TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y - 1, z)));
			} else {
				if (player.getInventory().hasItemId(ECUMENICAL_KEY)) {
					player.getInventory().remove(ECUMENICAL_KEY, 1);
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y + 1, z)));
					player.send(new SendMessage("You use your key and enter into chamber."));
					return true;
				}
				if (player.getMinigames().getGWKC()[Allegiance.ARMADYL.ordinal()] >= POINTS_TO_ENTER) {
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y + 1, z)));
					player.getMinigames().changeGWDKills(-POINTS_TO_ENTER, Allegiance.ARMADYL);
					for (Npc mob : NpcConstants.getGodWarsBossMob(Allegiance.ARMADYL)) {
						mob.getCombat().setAttacking(player);
						mob.getFollowing().setFollow(player, Following.FollowType.COMBAT);
					}
				} else {
					int req = POINTS_TO_ENTER - player.getMinigames().getGWKC()[Allegiance.ARMADYL.ordinal()];
					DialogueManager.sendStatement(player,
							"You need " + req + " more kill" + (req > 1 ? "s" : "") + " to enter this room.");
				}
			}
			return true;

		/**
		 * General Gaardor room
		 */
		case 26425:
			if (player.getX() <= 2863) {
				if (player.getInventory().hasItemId(ECUMENICAL_KEY)) {
					player.getInventory().remove(ECUMENICAL_KEY, 1);
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x + 1, y, z)));
					player.send(new SendMessage("You use your key and enter into chamber."));
					return true;
				}
				if (player.getMinigames().getGWKC()[Allegiance.BANDOS.ordinal()] >= POINTS_TO_ENTER) {
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x + 1, y, z)));
					player.getMinigames().changeGWDKills(-POINTS_TO_ENTER, Allegiance.BANDOS);
					for (Npc mob : NpcConstants.getGodWarsBossMob(Allegiance.BANDOS)) {
						mob.getCombat().setAttacking(player);
						mob.getFollowing().setFollow(player, Following.FollowType.COMBAT);
					}
				} else {
					int req = POINTS_TO_ENTER - player.getMinigames().getGWKC()[Allegiance.BANDOS.ordinal()];
					DialogueManager.sendStatement(player,
							"You need " + req + " more kill" + (req > 1 ? "s" : "") + " to enter this room.");
				}
			} else {
				TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x - 1, y, z)));
			}
			return true;

		/**
		 * Saradomin room
		 */
		case 26427:
			if (player.getX() >= 2908) {
				if (player.getInventory().hasItemId(ECUMENICAL_KEY)) {
					player.getInventory().remove(ECUMENICAL_KEY, 1);
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x - 1, y, z)));
					player.send(new SendMessage("You use your key and enter into chamber."));
					return true;
				}
				if (player.getMinigames().getGWKC()[Allegiance.SARADOMIN.ordinal()] >= POINTS_TO_ENTER) {
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x - 1, y, z)));
					player.getMinigames().changeGWDKills(-POINTS_TO_ENTER, Allegiance.SARADOMIN);
					for (Npc mob : NpcConstants.getGodWarsBossMob(Allegiance.SARADOMIN)) {
						mob.getCombat().setAttacking(player);
						mob.getFollowing().setFollow(player, Following.FollowType.COMBAT);
					}
				} else {
					int req = POINTS_TO_ENTER - player.getMinigames().getGWKC()[Allegiance.SARADOMIN.ordinal()];
					DialogueManager.sendStatement(player,
							"You need " + req + " more kill" + (req > 1 ? "s" : "") + " to enter this room.");
				}
			} else {
				TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x + 1, y, z)));
			}
			return true;

		/**
		 * Zamorak room
		 */
		case 26428:
			if (player.getY() >= 5332) {
				if (player.getInventory().hasItemId(ECUMENICAL_KEY)) {
					player.getInventory().remove(ECUMENICAL_KEY, 1);
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y - 1, z)));
					player.send(new SendMessage("You use your key and enter into chamber."));
					return true;
				}
				if (player.getMinigames().getGWKC()[Allegiance.ZAMORAK.ordinal()] >= POINTS_TO_ENTER) {
					TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y - 1, z)));
					player.getMinigames().changeGWDKills(-POINTS_TO_ENTER, Allegiance.ZAMORAK);
					for (Npc mob : NpcConstants.getGodWarsBossMob(Allegiance.BANDOS)) {
						mob.getCombat().setAttacking(player);
						mob.getFollowing().setFollow(player, Following.FollowType.COMBAT);
					}
				} else {
					int req = POINTS_TO_ENTER - player.getMinigames().getGWKC()[Allegiance.ZAMORAK.ordinal()];
					DialogueManager.sendStatement(player,
							"You need " + req + " more kill" + (req > 1 ? "s" : "") + " to enter this room.");
				}
			} else {
				TaskQueue.queue(new WalkThroughDoorTask(player, x, y, z, new Location(x, y + 1, z)));
			}
			return true;

		/**
		 * All altars
		 */

		case 26289:
		case 26287:
			if ((player.getAttributes().get(GWD_ALTAR_KEY) == null)
					|| (((Long) player.getAttributes().get(GWD_ALTAR_KEY)).longValue() < System.currentTimeMillis())) {
				player.getAttributes().set(GWD_ALTAR_KEY, Long.valueOf(System.currentTimeMillis() + 600_000));
				player.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You recharge your Prayer points at the altar."));
				player.getUpdateFlags().sendAnimation(645, 5);
				player.getLevels()[5] = player.getMaxLevels()[5];
				player.getSkill().update(5);
			} else {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You must wait a bit before praying at the altar again."));
			}
			return true;

		}
		return false;
	}

	/**
	 * Handles killing Godwars npc
	 * 
	 * @param player
	 * @param id
	 */
	public static void onGodwarsKill(Player player, int id) {
		GodWarsNpc npc = GodWarsData.forId(id);
		if (npc == null) {
			return;
		}
		player.getMinigames().changeGWDKills(1, npc.getAllegiance());
	}

	/**
	 * Use item on object
	 * 
	 * @param player
	 * @param id
	 * @param obj
	 * @return
	 */
	public static final boolean useItemOnObject(Player player, int id, int obj) {
		return false;
	}

}