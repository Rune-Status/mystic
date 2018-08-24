package org.mystic.game.model.content;

import java.util.Objects;

import org.mystic.game.model.content.dialogue.impl.$100ScrollDialogue;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

/**
 * Handles the membershop bond item interaction
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class $100Scroll {

	/**
	 * The membership bond item identification number
	 */
	public final static int $100_SCROLL = 608;

	/**
	 * Activates a players membership bond
	 * 
	 * @param player
	 *            the player activing the bond
	 * @param bond
	 *            the @{link Item} bond
	 */
	public static void activate(Player player, int $100_SCROLL) {
		if (Objects.nonNull(player) && player.getInventory().playerHasItem(new Item($100_SCROLL))) {
			player.start(new $100ScrollDialogue(player));
		}
	}
}
