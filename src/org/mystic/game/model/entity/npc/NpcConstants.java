package org.mystic.game.model.entity.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mystic.game.model.content.minigames.godwars.GodWarsData;
import org.mystic.game.model.content.minigames.godwars.GodWarsData.Allegiance;
import org.mystic.game.model.content.skill.fishing.FishingAction;
import org.mystic.game.model.definition.NpcDefinition;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.GameDefinitionLoader;

public final class NpcConstants {

	public static final List<Npc> GODWARS_BOSSES = new ArrayList<>();

	public static List<Npc> getGodWarsBossMob(Allegiance npc) {
		return GODWARS_BOSSES.stream().filter(
				mob -> GodWarsData.forId(mob.getId()) != null && GodWarsData.forId(mob.getId()).getAllegiance() == npc)
				.collect(Collectors.toList());
	}

	/**
	 * The random chance a mob will walk
	 */
	public static final byte RANDOM_WALK_CHANCE = 15;

	/**
	 * The maximum random walking distance from their location
	 */
	public static final byte MAX_RANDOM_WALK_DISTANCE = 5;

	/**
	 * The default respawn rate for npcs
	 */
	public static final byte DEFAULT_RESPAWN_TIME = 50;

	/**
	 * An array containing aggressive npcs
	 */
	private static final byte[] aggressive = new byte[18000];

	/**
	 * A list containing non aggressive npcs
	 */
	private static String[] NON_AGGRESSIVE_NPCS = { "man", "woman", "gnome", "cow", "guard", "goblin", "banker",
			"tzhaar-xil", "tzhaar-ket" };

	/**
	 * A list of mobs that don't follow
	 */
	private static final int[] NO_FOLLOW_MOBS = { 1457, 3943, 3847 };

	/**
	 * A list of flying mobs
	 */
	public static final int[] FLYING_MOBS = { 6230, 6229, 6231 };

	public static final int MAX_NPC_ID = 10000;

	static {
		for (int i = 0; i < 10000; i++) {
			NpcDefinition def = GameDefinitionLoader.getNpcDefinition(i);
			if ((def != null) && (def.getName() != null)) {
				String name = def.getName().toLowerCase();
				for (String k : NON_AGGRESSIVE_NPCS) {
					if (name.contains(k.toLowerCase())) {
						aggressive[i] = 1;
						break;
					}
				}
			}
		}
		System.out.println("Loaded non-aggressive mobs.");
	}

	public static final boolean face(int id) {
		return (id != 3636) && (FishingAction.FishingSpots.forId(id) == null);
	}

	public static final boolean isAggressive(int id) {
		return aggressive[id] != 1;
	}

	public static boolean isAgressiveFor(Npc mob, Player player) {
		if (mob.inWilderness()) {
			return true;
		}
		return ((player.getController().canAttackNPC())
				&& (player.getSkill().getCombatLevel() <= mob.getDefinition().getLevel() * 2 + 1));
	}

	/**
	 * Gets if the npc is a dagannoth king
	 * 
	 * @param m
	 *            The mob to check
	 * @return
	 */
	public static boolean isDagannothKing(Npc m) {
		return (m.getId() == 2881) || (m.getId() == 2882) || (m.getId() == 2883);
	}

	/**
	 * gets if the mob is a dragon or not
	 * 
	 * @param mob
	 * @return
	 */
	public static boolean isDragon(Npc mob) {
		if (mob == null) {
			return false;
		}
		return (mob.getId() == 51) || (mob.getId() == 53) || (mob.getId() == 54) || (mob.getId() == 55)
				|| (mob.getId() == 941) || (mob.getId() == 1590) || (mob.getId() == 1591) || (mob.getId() == 1592)
				|| (mob.getId() == 5363) || (mob.getId() == 50) || (mob.getId() == 10775) || mob.getId() == 50;
	}

	/**
	 * Gets if the mob shouldn't follow or not
	 * 
	 * @param mob
	 * @return
	 */
	public static final boolean noFollow(Npc mob) {
		for (int i : NO_FOLLOW_MOBS) {
			if (mob.getId() == i) {
				return true;
			}
		}
		return false;
	}
}