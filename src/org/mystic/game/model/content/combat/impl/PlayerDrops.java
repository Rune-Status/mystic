package org.mystic.game.model.content.combat.impl;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.dialogue.impl.Scavvo;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.player.ItemCheck;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.GameDefinitionLoader;

public class PlayerDrops {

	public static class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item arg0, Item arg1) {
			int value1 = GameDefinitionLoader.getHighAlchemyValue(arg0.getId());
			int value2 = GameDefinitionLoader.getHighAlchemyValue(arg1.getId());
			if (value1 > value2)
				return -1;
			if (value1 < value2) {
				return 1;
			}
			return 0;
		}
	}

	public static final ItemComparator ITEM_VALUE_COMPARATOR = new ItemComparator();

	public static final void dropItemsOnDeath(Player player) {
		if (player.getRights().equals(Rights.OWNER) || player.getRights().equals(Rights.ADMINISTRATOR)) {
			return;
		}
		Entity killer = player.getCombat().getDamageTracker().getKiller();
		int kept = player.isGoldMember() && !player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER) ? 5
				: 3;
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
		Queue<Item> items = new PriorityQueue<Item>(42, ITEM_VALUE_COMPARATOR);
		for (Item i : player.getInventory().getItems()) {
			if (i != null) {
				items.add(i);
			}
		}
		for (Item i : player.getEquipment().getItems()) {
			if (i != null) {
				items.add(i);
			}
		}
		Item k = null;
		player.getInventory().clear();
		player.getEquipment().clear();
		for (int i = 0; i < kept; i++) {
			Item keep = items.poll();
			if (keep != null) {
				if (keep.getAmount() == 1) {
					player.getInventory().add(keep, false);
				} else {
					player.getInventory().add(new Item(keep.getId(), 1), false);
					keep.remove(1);
					items.add(keep);
				}
			}
		}
		while ((k = items.poll()) != null) {
			if (k.getId() == 20085) {
				for (Item runes : player.getRunePouch().getItems()) {
					if (runes != null) {
						if (!killer.isNpc()
								&& IronMan.isIronMan(org.mystic.game.World.getPlayers()[killer.getIndex()])) {
							GroundItemHandler.add(runes, player.getLocation(), player);
						} else {
							GroundItemHandler.add(runes, player.getLocation(),
									(killer == null) || (killer.isNpc()) ? player
											: org.mystic.game.World.getPlayers()[killer.getIndex()]);
						}
					}
					player.getRunePouch().clear();
					player.getRunePouch().update();
				}
				player.send(new SendMessage("The runes from within your pouch fall to the ground."));
			}
			if ((k.getDefinition().isTradable()) || (ItemCheck.isItemDyed(k))) {
				if (k.getId() == 15443) {
					player.getInventory().add(1769, 1);
				} else if (k.getId() == 15442) {
					player.getInventory().add(1767, 1);
				} else if (k.getId() == 15441) {
					player.getInventory().add(1765, 1);
				} else if (k.getId() == 15444) {
					player.getInventory().add(1771, 1);
				} else if (k.getId() == 15701) {
					player.getInventory().add(1765, 1);
				} else if (k.getId() == 15702) {
					player.getInventory().add(1767, 1);
				} else if (k.getId() == 15703) {
					player.getInventory().add(1769, 1);
				} else if (k.getId() == 15704) {
					player.getInventory().add(1771, 1);
				}
				if (ItemCheck.isItemDyed(k)) {
					k.setId(4151);
				}
				Scavvo.doItemBreaking(k);
				if (!killer.isNpc() && IronMan.isIronMan(org.mystic.game.World.getPlayers()[killer.getIndex()])) {
					GroundItemHandler.add(k, player.getLocation(), player);
				} else {
					GroundItemHandler.add(k, player.getLocation(), (killer == null) || (killer.isNpc()) ? player
							: org.mystic.game.World.getPlayers()[killer.getIndex()]);
				}
			} else if (!k.getDefinition().isTradable() && !player.isGoldMember()) {
				GroundItemHandler.add(k, player.getLocation(), player);
			} else {
				player.getInventory().add(k, false);
			}
		}
		GroundItemHandler.add(new Item(526, 1), player.getLocation(),
				(killer == null) || (killer.isNpc()) ? player : org.mystic.game.World.getPlayers()[killer.getIndex()]);
		player.getInventory().update();
	}
}
