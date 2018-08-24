package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class DonatorShop3 extends Shop {

	public static final int SHOP_ID = 62;

	public static final int getPrice(int id) {
		switch (id) {
		case 10551:
			return 75;
		case 20072:
			return 75;
		case 6570:
			return 75;
		case 19111:
			return 750;
		case 15220:
			return 50;
		case 15019:
			return 50;
		case 15020:
			return 40;
		case 15018:
			return 30;
		case 18335:
			return 100;
		case 6585:
			return 50;
		case 6889:
			return 50;
		case 11724:
			return 250;
		case 11726:
			return 250;
		case 11718:
			return 100;
		case 11720:
			return 250;
		case 11722:
			return 250;
		case 13740:
			return 1000;
		case 13742:
			return 750;
		case 13738:
			return 350;
		case 13744:
			return 250;
		case 11283:
			return 150;
		case 11856:
		case 11854:
		case 11852:
		case 11850:
		case 11848:
		case 11846:
			return 70;

		}
		return 0;
	}

	public DonatorShop3() {

		super(SHOP_ID, new Item[] {

				new Item(10551, 1),

				new Item(20072, 1),

				new Item(6570, 1),

				new Item(19111, 1),

				new Item(15220, 1),

				new Item(15019, 1),

				new Item(15020, 1),

				new Item(15018, 1),

				new Item(6585, 1),

				new Item(6889, 1),

				new Item(11724, 1),

				new Item(11726, 1),

				new Item(11718, 1),

				new Item(11720, 1),

				new Item(11722, 1),

				new Item(13740, 1),

				new Item(13742, 1),

				new Item(13738, 1),

				new Item(13744, 1),

				new Item(11283, 1),

				new Item(11846, 1),

				new Item(11848, 1),

				new Item(11850, 1),

				new Item(11852, 1),

				new Item(11854, 1),

				new Item(11856, 1),

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
