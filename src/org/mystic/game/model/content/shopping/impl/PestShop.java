package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.player.io.PlayerLogs;

public class PestShop extends Shop {

	public static final int SHOP_ID = 89;

	public static final int getPrice(int id) {
		switch (id) {
		case 8842: // Void Gloves
			return 30;

		case 10499: // Ava's
			return 15;

		case 10548: // Penance Hats
		case 10547:
		case 10549:
		case 10550:
			return 30;

		case 10551: // Fighter Torso
			return 50;

		case 8839: // Void Robes
		case 8840:
			return 100;

		case 11674: // Void Helms
		case 11675:
		case 11676:
			return 70;

		}
		return 5000;
	}

	public PestShop() {
		super(SHOP_ID,
				new Item[] { new Item(11674, 1), new Item(11675, 1), new Item(11676, 1), new Item(8842, 1),
						new Item(8839, 1), new Item(8840, 1), new Item(8841, 1), new Item(10499, 1), new Item(10551, 1),
						new Item(10548, 1), new Item(10547, 1), new Item(10549, 1), new Item(10550, 1) },
				false, "Pest Control Rewards Shop");
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
		if (player.getPestPoints() < amount * getPrice(id)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough Pestcontrol points to buy that."));
			return;
		}
		player.setPestPoints(player.getPestPoints() - (amount * getPrice(id)));
		QuestTab.update(player);
		PlayerLogs.log(player.getUsername(),
				"Bought Item from shop (Id: " + buying.getId() + ", Amount: " + buying.getAmount() + ")");
		player.send(new SendString(
				"                                                   Reward Points: " + player.getPestPoints(), 3903));
		player.getInventory().add(buying);
		update();
	}

	@Override
	public int getBuyPrice(int id) {
		return 0;
	}

	@Override
	public String getCurrencyName() {
		return "Pest points";
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
