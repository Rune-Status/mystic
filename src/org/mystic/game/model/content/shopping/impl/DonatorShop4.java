package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class DonatorShop4 extends Shop {

	public static final int SHOP_ID = 63;

	public static final int getPrice(int id) {
		switch (id) {

		case 1042:
			return 1500;
		case 1048:
			return 1000;
		case 1038:
			return 950;
		case 1046:
			return 900;
		case 1044:
			return 850;
		case 1040:
			return 800;
		case 1050:
			return 600;
		case 1037:
			return 350;
		case 1055:
			return 750;
		case 1057:
			return 600;
		case 1053:
			return 500;

		}
		return 0;
	}

	public DonatorShop4() {

		super(SHOP_ID, new Item[] {

				new Item(1042, 1),

				new Item(1048, 1),

				new Item(1038, 1),

				new Item(1046, 1),

				new Item(1044, 1),

				new Item(1040, 1),

				new Item(1050, 1),

				new Item(1037, 1),

				new Item(1055, 1),

				new Item(1057, 1),

				new Item(1053, 1),

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
