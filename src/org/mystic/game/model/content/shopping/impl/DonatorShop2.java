package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class DonatorShop2 extends Shop {

	public static final int SHOP_ID = 61;

	public static final int getPrice(int id) {
		switch (id) {
		case 11694:
			return 350;
		case 11698:
			return 300;
		case 11700:
			return 250;
		case 11696:
			return 100;
		case 11730:
			return 60;
		case 4151:
			return 100;
		case 6914:
			return 60;
		case 15486:
			return 100;
		case 11235:
			return 50;
		case 20083:
			return 200;
		case 11061:
			return 150;

		}
		return 0;
	}

	public DonatorShop2() {

		super(SHOP_ID, new Item[] {

				new Item(11694, 1),

				new Item(11698, 1),

				new Item(11700, 1),

				new Item(11696, 1),

				new Item(11730, 1),

				new Item(4151, 1),

				new Item(6914, 1),

				new Item(15486, 1),

				new Item(11235, 1),

				new Item(20083, 1),

				new Item(11061, 1),

		}, false, "Mystic Store");
	}

	@Override
	public void buy(Player player, int slot, int id, int amount) {
		if (!hasItem(slot, id)) {
			return;
		}
		if (get(slot).getAmount() == 0) {
			return;
		}
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}
		Item buying = new Item(id, amount);
		if (!player.getInventory().hasSpaceFor(buying)) {
			if (!buying.getDefinition().isStackable()) {
				int slots = player.getInventory().getFreeSlots();
				if (slots > 0) {
					buying.setAmount(slots);
					amount = slots;
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough inventory space to buy this item."));
				}
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to buy this item."));
				return;
			}
		}
		if (player.getDonatorPoints() < amount * getPrice(id)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough Member Points to buy that."));
			return;
		}
		int set = player.getDonatorPoints() - amount * getPrice(id);
		player.setDonatorPoints(set);
		QuestTab.update(player);
		PlayerLogs.log(player.getUsername(),
				"Bought Item from shop (Id: " + buying.getId() + ", Amount: " + buying.getAmount() + ")");
		player.getInventory().add(buying);
		update();
	}

	@Override
	public int getBuyPrice(int id) {
		return 0;
	}

	@Override
	public String getCurrencyName() {
		return "Donator Points";
	}

	@Override
	public int getSellPrice(int id) {
		return getPrice(id);
	}

	@Override
	public boolean sell(Player player, int id, int amount) {
		player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell items to this shop."));
		return false;
	}
}
