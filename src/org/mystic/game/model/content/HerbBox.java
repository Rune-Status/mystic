package org.mystic.game.model.content;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class HerbBox {

	public static final int HERB_BOX = 6828;

	public static void open(Player player, int itemId) {
		if (player.getInventory().getFreeSlots() < 10) {
			player.send(new SendMessage("You do not have enough free inventory slots to do this."));
			return;
		}
		if (player.getInventory().playerHasItem(new Item(itemId))) {
			player.getInventory().remove(itemId);
			player.getInventory().add(new Item(202, player.isGoldMember() ? 20 : 15));
			player.getInventory().add(new Item(204, player.isGoldMember() ? 20 : 15));
			player.getInventory().add(new Item(206, player.isGoldMember() ? 20 : 15));
			player.getInventory().add(new Item(208, player.isGoldMember() ? 20 : 15));
			player.getInventory().add(new Item(210, player.isGoldMember() ? 15 : 10));
			player.getInventory().add(new Item(212, player.isGoldMember() ? 15 : 10));
			player.getInventory().add(new Item(214, player.isGoldMember() ? 15 : 10));
			player.getInventory().add(new Item(216, player.isGoldMember() ? 15 : 10));
			player.getInventory().add(new Item(218, player.isGoldMember() ? 15 : 10));
			player.getInventory().add(new Item(220, player.isGoldMember() ? 15 : 10));
		}
	}
}
