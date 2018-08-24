package org.mystic.game.model.content;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class CashCasket {

	public static final int CASKET_ID = 6759;

	public static void open(Player player, int itemId, int slot) {
		if (player.getInventory().playerHasItem(new Item(itemId, 1))) {
			if (itemId == CASKET_ID) {
				player.getInventory().clear(slot);
				player.getInventory().add(player.isGoldMember() ? new Item(995, 10000 + Misc.random(250000))
						: new Item(995, 5000 + Misc.random(150000)));
				player.send(new SendMessage("You open the casket and find some coins!"));
				player.getInventory().update();
			}
		}
	}

}
