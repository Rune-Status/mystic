package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class SlayerShop extends Shop {

	public static final int SHOP_ID = 93;

	public static final int getPrice(int id) {
		switch (id) {
		case 13263:
			return 200;
		case 15488:
			return 100;
		case 15490:
			return 100;
		case 18337:
			return 100;
		case 1767:
			return 75;
		case 1771:
			return 65;
		case 1769:
			return 75;
		case 1765:
			return 75;
		case 6828:
			return 40;
		case 6949:
			return 250;
		case 10551:
			return 225;
		case 2572:
			return 150;
		}
		return 0;
	}

	public SlayerShop() {
		super(SHOP_ID, new Item[] { new Item(13263), // slayer helm
				new Item(15488), // hexcrest
				new Item(15490), // focus sight
				new Item(18337), new Item(1767), new Item(1771), new Item(1769), new Item(1765), new Item(6828),
				new Item(6949), new Item(10551), new Item(2572),

		}, false, "Slayer Point Rewards");
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
		if (player.getSlayerPoints() < amount * getPrice(id)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough Slayer points to buy that."));
			return;
		}
		player.setSlayerPoints(player.getSlayerPoints() - amount * getPrice(id));
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
		return "Slayer points";
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
