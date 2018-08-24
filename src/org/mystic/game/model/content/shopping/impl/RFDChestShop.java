package org.mystic.game.model.content.shopping.impl;

import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class RFDChestShop extends Shop {

	public static final int RFD_CHEST_SHOP_ID = 98;

	public static Item[] getGloves() {
		Item[] gloves = new Item[10];
		for (int i = 0; i < gloves.length; i++) {
			gloves[i] = new Item(7453 + i);
		}
		return gloves;
	}

	public static boolean hasStageForItem(Player p, int item) {
		if (p.getQuesting().isQuestCompleted(QuestConstants.RECIPE_FOR_DISASTER)) {
			return true;
		}
		byte stage = p.getQuesting().getQuestStage(QuestConstants.RECIPE_FOR_DISASTER);
		switch (item) {
		case 7462:
			return false;
		case 7460:
		case 7461:
			return stage >= 4;
		case 7459:
			return stage >= 3;
		case 7458:
			return stage >= 2;
		}
		return stage >= 1;
	}

	public RFDChestShop() {
		super(98, getGloves(), false, "Gloves");
	}

	@Override
	public void buy(Player player, int slot, int id, int amount) {
		if (!hasItem(slot, id))
			return;
		if (get(slot).getAmount() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("The shop is out of stock on that item."));
			return;
		}
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}

		if (!hasStageForItem(player, id)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have not defeated the required monster to buy this item."));
			return;
		}

		Item buying = new Item(id, amount);

		Item gold = new Item(995, getSellPrice(id) * amount);

		if (!player.getInventory().hasSpaceOnRemove(gold, buying)) {
			if (!buying.getDefinition().isStackable()) {
				int slots = player.getInventory().getFreeSlots();
				if (slots > 0) {
					buying.setAmount(slots);
					amount = slots;
					gold.setAmount(getSellPrice(id) * amount);
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

		if (!player.getInventory().hasItemAmount(gold)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough coins to buy that."));
			return;
		}

		player.getInventory().remove(gold, false);

		player.getInventory().add(buying);
		update();
	}

	@Override
	public boolean sell(Player player, int id, int amount) {
		player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell items to this shop."));
		return false;
	}
}