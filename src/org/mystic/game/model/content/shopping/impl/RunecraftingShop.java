package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class RunecraftingShop extends Shop {

	public static final int SHOP_ID = 77;

	public static final int getPrice(int id) {
		switch (id) {
		case 13615:
		case 13614:
		case 13617:
		case 13618:
		case 13620:
		case 13619:
		case 13622:
		case 13623:
		case 13625:
		case 13624:
		case 13627:
		case 13628:
			return 625;

		case 5512:
			return 600;

		case 5510:
			return 400;

		case 5509:
			return 200;
		}
		return 0;
	}

	public RunecraftingShop() {

		super(SHOP_ID, new Item[] {

				new Item(13615, 1),

				new Item(13614, 1),

				new Item(13617, 1),

				new Item(13618, 1),

				new Item(13620, 1),

				new Item(13619, 1),

				new Item(13622, 1),

				new Item(13623, 1),

				new Item(13625, 1),

				new Item(13624, 1),

				new Item(13627, 1),

				new Item(13628, 1),

				new Item(5509, 1),

				new Item(5510, 1),

				new Item(5512, 1),

		}, false, "Runecrafting Rewards");
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
		if (player.getRunecraftingPoints() < amount * getPrice(id)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough Runecrafting Points to buy that."));
			return;
		}
		int set = player.getRunecraftingPoints() - amount * getPrice(id);
		player.setRunecraftingPoints(set);
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
		return "Runecrafting Points";
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
