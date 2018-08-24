package org.mystic.game.model.content.combat.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * Manages the rewards and annoucement of achieving a killstreak
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class Killstreaks {

	/**
	 * Announces the killers streak to the server
	 * 
	 * @param player
	 *            the player on the kill streak
	 */
	private static void announce(Player player) {
		if (player.getKillstreak() > 1) {
			String string = "@dre@[Killstreaks: Player '" + player.getUsername() + "' is now on a killstreak of "
					+ player.getKillstreak() + ". Kill them for a reward]";
			World.sendGlobalMessage(string, false);
		}
	}

	/**
	 * Increases the players killstreak and calls to announce to the to the server
	 * 
	 * @param player
	 *            the player we are increasing for
	 */
	public static void increase(Player player) {
		player.setKillstreak(player.getKillstreak() + 1);
		QuestTab.update(player);
		announce(player);
	}

	public static void reset(Player killed, Entity killer) {
		if (killer == null) {
			return;
		}
		if (!killer.isNpc()) {
			if (killed.getKillstreak() >= 2) {
				killed.send(new SendMessage("@dre@Your killstreak has come to an end and it's all thanks to '"
						+ killer.getPlayer().getUsername() + "'."));
				killer.getPlayer().getEarningPotential().addPKP(3);
				killer.getPlayer().send(
						new SendMessage("@dre@You receive a reward of 3 Pk Points for ending a players killstreak."));
				World.sendGlobalMessage(killer.getPlayer().getUsername() + " has just ended " + killed.getUsername()
						+ "'s killstreak of " + killed.getKillstreak() + "!", false);
			}
			killed.setKillstreak(0);
			QuestTab.update(killed);
			QuestTab.update(killer.getPlayer());
		}
	}

}