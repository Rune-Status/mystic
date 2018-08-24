package org.mystic.game.model.content.skill.agility;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.agility.gnome.BalanceOverObject;
import org.mystic.game.model.content.skill.agility.gnome.ClimbOverTask;
import org.mystic.game.model.content.skill.agility.gnome.ClimbThroughPipe;
import org.mystic.game.model.content.skill.agility.wilderness.ClimbRockTask;
import org.mystic.game.model.content.skill.agility.wilderness.GateDoorEnter;
import org.mystic.game.model.content.skill.agility.wilderness.GateDoorExit;
import org.mystic.game.model.content.skill.agility.wilderness.SteppingStonesTask;
import org.mystic.game.model.content.skill.agility.wilderness.WalkOverLog;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.TaskQueue;

/**
 * Manages the execution of agility tasks
 */
public class Agility {

	public static final int TICKET = 2996;

	public static final String[] TICKET_KEYS = { "gnometicketkeya", "gnometicketkeyb", "gnometicketkeyc",
			"gnometicketkeyd", "gnometicketkeye", "gnometicketkeyf", "gnometicketkeyg" };

	private static void setCompleted(Player p, int i) {
		p.getAttributes().set(TICKET_KEYS[i], Byte.valueOf((byte) 0));
	}

	private static boolean hasCompleted(Player p) {
		for (String i : TICKET_KEYS) {
			if (p.getAttributes().get(i) == null) {
				return false;
			}
		}
		return true;
	}

	public static void finish(Player player) {
		if (hasCompleted(player)) {
			player.getInventory().addOrCreateGroundItem(2996, player.isGoldMember() ? 2 : 1, true);
			player.getSkill().addExperience(Skills.AGILITY, 450.0D);
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You have completed the course and earned a ticket."));

			player.getAchievements().incr(player, "Finish Gnome agility course");
			if (wearingAgile(player)) {
				player.getSkill().addExperience(Skills.AGILITY, 100.0D);
				player.getClient()
						.queueOutgoingPacket(new SendMessage("Your agile armor gains you some bonus experience."));
			}
		} else {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You did not cross every obstacle, you must complete the course for a reward."));
		}
		for (String i : TICKET_KEYS) {
			player.getAttributes().remove(i);
		}
	}

	public static final String[] TICKET_KEYS_WILD = { "wildticketkeya", "wildticketkeyb", "wildticketkeyc",
			"wildticketkeyd", "wildticketkeye" };

	private static void setCompletedWild(Player p, int i) {
		p.getAttributes().set(TICKET_KEYS_WILD[i], Byte.valueOf((byte) 0));
	}

	private static boolean hasCompletedWild(Player p) {
		for (String i : TICKET_KEYS_WILD) {
			if (p.getAttributes().get(i) == null) {
				return false;
			}
		}
		return true;
	}

	public static boolean wearingAgile(Player player) {
		return player.getEquipment().isWearingItem(14938) && player.getEquipment().isWearingItem(14936);
	}

	public static void finishWild(Player player) {
		if (hasCompletedWild(player)) {
			player.getInventory().addOrCreateGroundItem(2996, player.isGoldMember() ? 2 : 1, true);
			player.getSkill().addExperience(Skills.AGILITY, 900.0D);
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You have completed the course and earned a ticket."));
			player.getAchievements().incr(player, "Finish Wilderness course");
			if (wearingAgile(player)) {
				player.getSkill().addExperience(Skills.AGILITY, 200.0D);
				player.getClient()
						.queueOutgoingPacket(new SendMessage("Your agile armor yields you some bonus experience."));
			}
		} else {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You did not cross every obstacle, you must complete the course for a reward."));
		}
		for (String i : TICKET_KEYS_WILD) {
			player.getAttributes().remove(i);
		}
	}

	/**
	 * Executes the agility task corresponding to the object interacted with
	 * 
	 * @param player
	 *            the player interacting with the object
	 * @param object
	 *            the object being interacted with
	 */
	public static boolean execute(Player player, GameObject object) {
		if (player.getSkill().locked()) {
			return false;
		}
		switch (object.getId()) {
		/**
		 * Mining guild *
		 */
		case 9325:
			if (hasReq(player, 20)) {
				if (object.getLocation().getX() == 3034 && object.getLocation().getY() == 9806) {
					player.getSkill().lock(10);
					TaskQueue.queue(new ClimbThroughPipe(player, new Location(3028, 9806), new Location(3035, 9806),
							100.5D, false));
				}
				if (object.getLocation().getX() == 3029 && object.getLocation().getY() == 9806) {
					player.getSkill().lock(10);
					TaskQueue.queue(new ClimbThroughPipe(player, new Location(3035, 9806), new Location(3028, 9806),
							100.5D, false));
				}
			}
			break;

		/**
		 * Taverly dungeon *
		 */
		case 9293:
			if (hasReq(player, 70)) {
				if (object.getLocation().getX() == 2887 && object.getLocation().getY() == 9799) {
					player.getSkill().lock(10);
					TaskQueue.queue(new ClimbThroughPipe(player, new Location(2892, 9799), new Location(2886, 9799),
							7.5D, false));
				}
				if (object.getLocation().getX() == 2891 && object.getLocation().getY() == 9799) {
					player.getSkill().lock(10);
					TaskQueue.queue(new ClimbThroughPipe(player, new Location(2886, 9799), new Location(2892, 9799),
							7.5D, false));
				}
			}
			break;

		/**
		 * The start of the gnome agility course
		 */
		case 2295:
			player.getSkill().lock(4);
			TaskQueue.queue(new BalanceOverObject(player, new Location(2474, 3429), 10.5D));
			setCompleted(player, 0);
			return true;
		case 2285:
			player.getSkill().lock(4);
			TaskQueue.queue(new ClimbOverTask(player, 1, new Location(2473, 3424, 1), 828, 8.0D));
			setCompleted(player, 1);
			return true;
		case 2313:
			player.getSkill().lock(4);
			TaskQueue.queue(new ClimbOverTask(player, 1, new Location(2473, 3420, 2), 828, 8.0D));
			setCompleted(player, 2);
			return true;
		case 2312:
			player.getSkill().lock(4);
			TaskQueue.queue(new BalanceOverObject(player, new Location(2483, 3420, 2), 10.5D));
			setCompleted(player, 3);
			return true;
		case 2314:
			player.getSkill().lock(4);
			TaskQueue.queue(new ClimbOverTask(player, 1, new Location(2486, 3422, 0), 828, 8.0D));
			setCompleted(player, 4);
			return true;
		case 2286:
			player.getSkill().lock(4);
			TaskQueue.queue(new ClimbOverTask(player, 1,
					new Location(player.getLocation().getX(), player.getLocation().getY() + 2, 0), 828, 6.0D));
			setCompleted(player, 5);
			return true;
		case 154:
			if (player.getLocation().getY() > 3432) {
				return false;

			}
			player.getSkill().lock(4);
			if (player.getX() != 2484 && player.getY() != 3430) {
				player.getMovementHandler().addToPath(new Location(2484, 3430));
			}
			TaskQueue.queue(
					new ClimbThroughPipe(player, new Location(2484, 3437), new Location(2484, 3430), 7.5D, true));
			setCompleted(player, 6);
			return true;
		case 4058:
			if (player.getLocation().getY() > 3432) {
				return false;

			}
			player.getSkill().lock(4);
			if (player.getX() != 2487 && player.getY() != 3432) {
				player.getMovementHandler().addToPath(new Location(2484, 3432));
			}
			TaskQueue.queue(
					new ClimbThroughPipe(player, new Location(2487, 3437), new Location(2487, 3432), 7.5D, true));
			setCompleted(player, 6);
			return true;
		/**
		 * The end of the gnome agility course
		 */

		//

		/**
		 * The start of the wilderness course
		 */
		case 2288:
			if (player.getLocation().getY() > 3937) {
				return false;
			}
			player.getSkill().lock(4);
			if (player.getX() != 3004 && player.getY() != 3937) {
				player.getMovementHandler().addToPath(new Location(3004, 3937));
			}
			TaskQueue.queue(
					new ClimbThroughPipe(player, new Location(3004, 3950), new Location(3004, 3937), 22.5D, false));
			setCompletedWild(player, 0);
			return true;
		case 2283:
			if (player.getLocation().getY() > 3954) {
				return false;

			}
			player.getSkill().lock(4);
			player.teleport(new Location(3005, 3958, 0));
			setCompletedWild(player, 1);
			return true;
		case 4936:
			if (player.getSkill().locked()) {
				return false;
			}
			player.getSkill().lock(6);
			TaskQueue.queue(new SteppingStonesTask(player, new Location(2996, 3960)));
			setCompletedWild(player, 2);
			return true;
		case 2297:
			player.getSkill().lock(4);
			player.teleport(new Location(3002, 3945));
			TaskQueue.queue(new WalkOverLog(player, new Location(2994, 3945), 30.5D));
			setCompletedWild(player, 3);
			return true;
		case 2328:
			player.getSkill().lock(4);
			TaskQueue.queue(new ClimbRockTask(player,
					new Location(player.getLocation().getX(), player.getLocation().getY() - 5, 0), 20.0D));
			setCompletedWild(player, 4);
			return true;
		case 2307:
			if (player.getX() != 2998 && player.getY() == 3931) {
				player.getMovementHandler().addToPath(new Location(2998, 3931));
			}
			if (player.getX() == 2998 && player.getY() == 3931) {
				player.getSkill().lock(4);
				TaskQueue.queue(new GateDoorExit(player, player.getLocation().getX(), player.getLocation().getY(),
						player.getLocation().getZ(), new Location(player.getLocation().getX(),
								player.getLocation().getY() - 1, player.getLocation().getZ())));
			}
			return true;
		case 2309:
			if (hasReq(player, 52)) {
				player.getSkill().lock(4);
				TaskQueue.queue(new GateDoorEnter(player, object.getLocation().getX(), object.getLocation().getY(),
						object.getLocation().getZ(), new Location(object.getLocation().getX(),
								object.getLocation().getY(), object.getLocation().getZ())));
			}
			return true;
		/**
		 * The end of the wilderness course
		 */
		}
		return false;
	}

	public static boolean hasReq(Player player, int req) {
		if (player.getSkill().getLevels()[Skills.AGILITY] >= req) {
			return true;
		} else {
			DialogueManager.sendStatement(player, "You need an Agility level of " + req + " to tackle this obstacle.");
			player.send(new SendMessage("You need an Agility level of " + req + " to tackle this obstacle."));
			return false;
		}
	}

}