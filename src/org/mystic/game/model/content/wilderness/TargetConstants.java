package org.mystic.game.model.content.wilderness;

import java.util.Objects;

import org.mystic.game.model.entity.player.Player;

/**
 * Holds all the constants for the Wilderness Targets System
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class TargetConstants {

	/**
	 * The time in 600ms ticks for a player to receieve a target
	 */
	public static final int TARGET_TIME = 150;

	/**
	 * The time in 600ms ticks for a player to gain an EP increase
	 */
	public static final int EP_TIME = 500;

	/**
	 * Determines if the chosen user is an applicable choice for the other user to
	 * be assigned a target to.
	 * 
	 * @param player
	 *            the player that is getting set with a target
	 * @param target
	 *            the player we are checking to see if they are an appropriate fit
	 *            as a target
	 * @return able or not to get a target
	 */
	public static boolean able(Player player, Player target) {
		if (Objects.isNull(player) || Objects.isNull(target)) {
			return false;
		}
		if (!player.isActive() || !target.isActive()) {
			return false;
		}
		if (player == target) {
			return false;
		}
		if (player.getIndex() == target.getIndex()) {
			return false;
		}
		if (player == target || player.getUsername() == target.getUsername()) {
			return false;
		}
		if (player.getTarget() != null || target.getTarget() != null) {
			return false;
		}
		if (!player.inWilderness() || !target.inWilderness()) {
			return false;
		}
		if (!goodDifference(player, target)) {
			return false;
		}
		return true;
	}

	public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
		if (combatLevel > otherCombatLevel) {
			return (combatLevel - otherCombatLevel);
		} else if (otherCombatLevel > combatLevel) {
			return (otherCombatLevel - combatLevel);
		} else {
			return 0;
		}
	}

	public static boolean goodDifference(Player player, Player target) {
		int combatDifference = combatLevelDifference(player.getSkill().getCombatLevel(),
				target.getSkill().getCombatLevel());
		return combatDifference <= player.getWildernessLevel() && combatDifference <= target.getWildernessLevel();
	}

}