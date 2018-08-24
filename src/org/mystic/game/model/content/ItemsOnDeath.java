package org.mystic.game.model.content;

import java.util.PriorityQueue;
import java.util.Queue;

import org.mystic.game.model.content.combat.impl.PlayerDrops;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;

public class ItemsOnDeath {

	public static void display(Player player) {
		int kept = 3;
		if (player.getSkulling().isSkulled()) {
			kept = 0;
		}
		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(10)) {
				kept++;
			}
		} else if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES) {
			if (player.getPrayer().active(0)) {
				kept++;
			}
		}
		Queue<Item> dropItems = new PriorityQueue<Item>(42, PlayerDrops.ITEM_VALUE_COMPARATOR);
		for (Item i : player.getInventory().getItems()) {
			if (i != null) {
				dropItems.add(new Item(i.getId(), i.getAmount()));
			}
		}
		for (Item i : player.getEquipment().getItems()) {
			if (i != null) {
				dropItems.add(new Item(i.getId(), i.getAmount()));
			}
		}
		Item dropItem = null;
		Item[] toKeep = new Item[kept];
		int keepIndex = 0;
		for (int i = 0; i < kept; i++) {
			Item keep = dropItems.poll();
			if (keep != null) {
				if (keep.getAmount() == 1) {
					toKeep[keepIndex++] = keep;
				} else {
					keep.remove(1);
					toKeep[keepIndex++] = new Item(keep.getId(), 1);
				}
			}
		}
		Item[] toDrop = new Item[dropItems.size()];
		int dropIndex = 0;
		while ((dropItem = dropItems.poll()) != null) {
			if (dropItem.getDefinition().isTradable() || !dropItem.getDefinition().isTradable()
					|| ItemCheck.isItemDyed(dropItem)) {
				toDrop[dropIndex++] = dropItem;
			}
		}
		player.send(new SendUpdateItems(10494, toKeep));
		player.send(new SendUpdateItems(10600, toDrop));
		player.send(new SendInterface(19100));
	}

}