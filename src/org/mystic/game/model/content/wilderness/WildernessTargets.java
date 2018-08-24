package org.mystic.game.model.content.wilderness;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPlayerHint;

/**
 * Manages the Wilderness Target Content
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class WildernessTargets {

	/**
	 * Assigns both given players as targets to eachother
	 * 
	 * @param player
	 *            the player getting their target
	 * @param target
	 *            the player chosen as a target
	 */
	public static void assignTargets(Player player, Player target) {
		if (player == null || target == null || player == target) {
			return;
		}
		target.getClient().queueOutgoingPacket(new SendMessage(
				"You have been assigned the user " + "@dre@'" + player.getUsername() + "'@bla@" + " as your target!"));
		player.getClient().queueOutgoingPacket(new SendMessage(
				"You have been assigned the user " + "@dre@'" + target.getUsername() + "'@bla@" + " as your target!"));
		if (World.getPlayers()[target.getIndex()] != null) {
			player.getClient().queueOutgoingPacket(new SendPlayerHint(true, target.getIndex()));
		}
		if (World.getPlayers()[player.getIndex()] != null) {
			target.getClient().queueOutgoingPacket(new SendPlayerHint(true, player.getIndex()));
		}
		target.setTarget(player);
		player.setTarget(target);

	}

	/**
	 * Searchs all active players and finds a target for the player
	 * 
	 * @param player
	 *            The player we are searching for
	 */
	public static void findTarget(Player player) {
		for (Player target : World.getPlayers()) {
			if (target == null) {
				continue;
			}
			if (TargetConstants.able(player, target)) {
				WildernessTargets.assignTargets(player, target);
			}
		}
		return;
	}

	/**
	 * Resets a players wildernes target either for when logging out or for when
	 * their current target has died
	 * 
	 * @param player
	 *            the player we are resetting for
	 */
	public static void resetTarget(Player player, Player target, boolean logout) {
		if (player == null || target == null) {
			return;
		}
		if (logout) {
			target.getClient().queueOutgoingPacket(
					new SendMessage("@dre@Your target has left this world, you will be assigned a new one shortly."));
		} else {
			target.getClient().queueOutgoingPacket(new SendMessage(
					"@dre@Your target has left the wilderness, you will be assigned a new one shortly."));
		}
		target.targetTimer = 30;
		target.setTarget(null);
		player.setTarget(null);
		target.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));
		player.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));
	}
}