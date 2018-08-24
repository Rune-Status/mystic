package org.mystic.game.model.content.minigames.clanwars;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * Safe Wars Minigame Managament
 * 
 * @author Valii - http://www.rune-server.org/members/Valiant
 *
 */
public class SafeWars {

	/**
	 * Handles the interaction with the safe wars portal
	 * 
	 * @param player
	 *            the player interacting with the object
	 * @param exiting
	 *            is the player exiting the portal
	 */
	public static void interactPortal(Player player, boolean exiting) {
		if (exiting) {
			player.teleport(new Location(3086, 3485, 0));
			player.send(new SendMessage("You step through the portal and exit safe clan wars."));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			if (player.getSkulling().isSkulled()) {
				player.getSkulling().unskull(player);
			}
			player.getSkill().restore();
		} else {
			player.setController(ControllerManager.SAFE_WARS);
			player.teleport(new Location(2815, 5511, 0));
			player.send(new SendMessage("You step through the portal and enter into the safe clan wars zone."));
		}
	}

}
