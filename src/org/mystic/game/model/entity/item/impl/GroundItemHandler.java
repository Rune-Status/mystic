package org.mystic.game.model.entity.item.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class GroundItemHandler {

	public static final int SHOW_GROUND_ITEM = 100;

	public static final int REMOVE_GROUND_ITEM = 350;

	public static final int MAX_GLOBALIZATION = 10;

	public static final int MAX_REMOVAL = 10;

	private static final List<GroundItem> active = new LinkedList<GroundItem>();

	private static final List<GroundItem> globalizeQueue = new LinkedList<GroundItem>();

	public static boolean add(GroundItem groundItem) {
		Player owner = groundItem.getOwner();
		if ((owner != null) && (owner.getGroundItems().stack(groundItem))) {
			return true;
		}
		if ((owner != null) && (visible(owner, groundItem))) {
			owner.getGroundItems().add(groundItem);
		}
		active.add(groundItem);
		return true;
	}

	public static boolean add(Item item, Location location, Player player) {
		GroundItem groundItem = new GroundItem(item, new Location(location),
				player == null ? null : player.getUsername());
		add(groundItem);
		return true;
	}

	public static boolean add(Item item, Location location, Player player, int time) {
		GroundItem groundItem = new GroundItem(item, new Location(location),
				player == null ? null : player.getUsername());
		if (time >= 0) {
			groundItem.setTime(time);
		}
		return add(groundItem);
	}

	public static boolean exists(GroundItem g) {
		return active.contains(g);
	}

	public static List<GroundItem> getActive() {
		return active;
	}

	/**
	 * name = player username your searching for specific = if this item is owned by
	 * this player
	 */
	public static GroundItem getGroundItem(int id, int x, int y, int z, String name, boolean specific) {
		long longAsName = name == null ? -1 : Misc.nameToLong(name);
		Location l = new Location(x, y, z);
		for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
			GroundItem item = i.next();
			if (item.getLocation().equals(l) && item.exists()) {
				if (longAsName != -1 && longAsName == item.getLongOwnerName() && item.getItem().getId() == id
						|| !specific && item.isGlobal() && item.getItem().getId() == id) {
					return item;
				}
			}
		}
		return null;
	}

	public static GroundItem getNonGlobalGroundItem(int id, int x, int y, int z, long name) {
		Location l = new Location(x, y, z);
		for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
			GroundItem item = i.next();
			if ((item.getLocation().equals(l)) && (!item.isGlobal()) && (item.exists())) {
				if ((item.getLongOwnerName() == name) && (item.getItem().getId() == id)) {
					return item;
				}
			}
		}
		return null;
	}

	public static Region getRegion(GroundItem item) {
		return Region.getRegion(item.getLocation().getX(), item.getLocation().getY());
	}

	public static void globalize(GroundItem item) {
		globalizeQueue.add(item);
	}

	public static void process() {
		for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
			GroundItem item = i.next();
			item.countdown();
			if (item.globalize()) {
				globalize(item);
			}
			if (item.remove()) {
				item.erase();
				if (!item.isGlobal()) {
					Player owner = item.getOwner();
					if ((owner != null) && (visible(owner, item))) {
						owner.getGroundItems().remove(item);
					}
				} else {
					for (int k = 1; k < World.getPlayers().length; k++) {
						Player player = World.getPlayers()[k];
						if (player != null) {
							if (visible(player, item)) {
								player.getGroundItems().remove(item);
							}
						}
					}
				}
				i.remove();
			}
		}
		for (Iterator<GroundItem> i = globalizeQueue.iterator(); i.hasNext();) {
			GroundItem groundItem = i.next();
			if (!groundItem.exists()) {
				i.remove();
			} else {
				groundItem.setGlobal(true);
				Player owner = groundItem.getOwner();
				for (int k = 1; k < World.getPlayers().length; k++) {
					Player player = World.getPlayers()[k];
					if ((player != null) && ((owner == null) || (!player.equals(owner)))) {
						if (visible(player, groundItem)) {
							player.getGroundItems().add(groundItem);
						}
					}
				}
				i.remove();
			}
		}
	}

	public static boolean remove(GroundItem item) {
		if (item.isGlobal) {
			GlobalItemHandler.respawn(item);
		}
		item.erase();
		if (!item.isGlobal()) {
			Player owner = item.getOwner();
			if ((owner != null) && (visible(owner, item))) {
				owner.getGroundItems().remove(item);
			}
		} else {
			for (int k = 1; k < World.getPlayers().length; k++) {
				Player player = World.getPlayers()[k];
				if (player != null) {
					if (visible(player, item)) {
						player.getGroundItems().remove(item);
					}
				}
			}
		}
		active.remove(item);
		return true;
	}

	public static boolean visible(Player player, GroundItem item) {
		Player owner = item.getOwner();
		if ((player.withinRegion(item.getLocation())) && (player.getLocation().getZ() == item.getLocation().getZ())
				&& ((item.isGlobal()) || ((owner != null) && (player.equals(owner))))) {
			return true;
		}
		return false;
	}
}
