package org.mystic.game.model.content;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.impl.ActivateBondDialogue;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

/**
 * Handles the membershop bond item interaction
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class MembershipBond {

	/**
	 * The membership bond item identification number
	 */
	public final static int MEMBERSHIP_BOND_ID = 20086;

	/**
	 * Activates a players membership bond
	 * 
	 * @param player
	 *            the player activing the bond
	 * @param bond
	 *            the @{link Item} bond
	 */
	public static void activate(Player player, int bond) {
		if (player.getInventory().playerHasItem(new Item(bond))) {
			if (player.isGoldMember()) {
				DialogueManager.sendStatement(player, "You are already a Member.", "This bond has no effect on you.");
				return;
			} else {
				player.start(new ActivateBondDialogue(player));
			}
		}
	}

}
