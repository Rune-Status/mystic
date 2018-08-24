package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.player.io.PlayerLogs;

public class VoteShop extends Shop {

	public static final int SHOP_ID = 95;

	public static final int getPrice(int id) {
		switch (id) {
		case 989:
			return 10;
		case 10551:
		case 20072:
			return 300;
		case 6570:
			return 500;
		case 1769:
		case 1765:
		case 1767:
		case 1771:
			return 100;
		case 2572:
			return 40;
		case 18744:
		case 18745:
		case 18746:
			return 200;
		case 19333:
			return 180;
		case 6949:
			return 250;
		case 19111:
			return 1000;

		}
		return 5;
	}

	public VoteShop() {
		super(SHOP_ID, new Item[] { new Item(10551, 1), new Item(6570, 1), new Item(19111, 1), new Item(20072, 1),
				new Item(1765, 1), new Item(1767, 1), new Item(1771, 1), new Item(1769, 1), new Item(989, 1),
				new Item(2572, 1),

				new Item(18745, 1), new Item(18746, 1), new Item(18744, 1), new Item(19333, 1), new Item(6949, 1),

		}, false, "Mystic Voting Rewards");
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
		if (player.getVotePoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough Vote points to buy that."));
			return;
		}
		player.setVotePoints(player.getVotePoints() - amount * getPrice(id));
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
		return "Vote points";
	}

	@Override
	public int getSellPrice(int id) {
		return getPrice(id);
	}

	@Override
	public boolean sell(Player player, int id, int amount) {
		player.getClient().queueOutgoingPacket(new SendMessage("You can't sell items to this shop."));
		return false;
	}
}
