package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.player.io.PlayerLogs;

public class PKPShop extends Shop {

	public static final int SHOP_ID = 99;

	public static final int getPrice(int id) {
		switch (id) {

		case 8850: // Rune Defender return 15;
		case 20072: // Dragon Defender
			return 25;

		case 6570: // Fire Cape
		case 6585: // Amulet Of Fury return 30;
		case 19111: // Tokhaar-Kal return 250;
		case 15017: // Onyx Ring (i) return 45;
		case 15220: // Berserker Ring (i)
		case 15020: // Warrior Ring (i)
		case 15019: // Archers' Ring (i)
		case 15018: // Seers' Ring (i)
			return 50;

		case 6914: // Master Wand
		case 2579: // Wizard Boots
		case 6920: // Infinity Boots
		case 10551: // Fighter Torso
		case 10547: // Healer Hat
		case 10548: // Fighter Hat
			return 50;

		case 18335: // Arcane Stream Necklace
			return 250;

		case 11061: // Ancient Mace
			return 500;

		}
		return 2147483647;
	}

	public PKPShop() {

		super(SHOP_ID, new Item[] {

				new Item(11061, 1),

				new Item(8850, 1),

				new Item(20072, 1),

				new Item(6570, 1),

				new Item(19111, 1),

				new Item(12964, 1),

				new Item(12971, 1),

				new Item(12978, 1),

				new Item(10548, 1),

				new Item(10549, 1),

				new Item(10551, 1),

				new Item(15345, 1),

				new Item(15347, 1),

				new Item(12866, 1),

				new Item(12873, 1),

				new Item(12880, 1),

				new Item(10547, 1),

				new Item(10550, 1),

				new Item(10555, 1),

				new Item(15349, 1),

				new Item(19748, 1),

				new Item(12887, 1),

				new Item(12894, 1),

				new Item(12901, 1),

				new Item(18335, 1),

				new Item(18346, 1),

				new Item(15602, 1),

				new Item(6737, 1),

				new Item(6735, 1),

				new Item(4224, 1),

				new Item(12922, 1),

				new Item(12908, 1),

				new Item(11061, 1),

				new Item(13899, 1),

				new Item(15614, 1),

				new Item(6733, 1),

				new Item(6731, 1),

				new Item(4212, 1),

				new Item(12929, 1),

				new Item(12915, 1),

		}, false, "Wilderness Rewards");
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
		if (player.getEarningPotential().getPkp() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough PKP to buy that."));
			return;
		}
		player.getEarningPotential().decreasePKP(amount * getPrice(id));
		QuestTab.update(player);
		PlayerLogs.log(player.getUsername(),
				"Bought Item from shop (Id: " + buying.getId() + ", Amount: " + buying.getAmount() + ")");
		player.getInventory().add(buying);
		player.send(new SendString("                                                   Reward Points: "
				+ player.getEarningPotential().getPkp(), 3903));
		update();
	}

	@Override
	public int getBuyPrice(int id) {
		return 0;
	}

	@Override
	public String getCurrencyName() {
		return "PKP";
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
