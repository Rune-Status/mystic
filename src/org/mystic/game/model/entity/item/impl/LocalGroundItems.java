package org.mystic.game.model.entity.item.impl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.dialogue.impl.Scavvo;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.pathfinding.StraightPathFinder;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendGroundItem;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveGroundItem;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.WalkToTask;

public class LocalGroundItems {

	private final Player player;

	private Deque<GroundItem> loaded = new ArrayDeque<GroundItem>();

	private Deque<GroundItem> adding = new ArrayDeque<GroundItem>();

	private Deque<GroundItem> removing = new ArrayDeque<GroundItem>();

	private boolean hasLoaded = true;

	public LocalGroundItems(Player player) {
		this.player = player;
	}

	public void add(GroundItem groundItem) {
		adding.add(groundItem);
	}

	public void drop(int id, int slot) {
		final Item drop = new Item(player.getInventory().get(slot));
		Scavvo.doItemBreaking(drop);
		GroundItemHandler.add(drop, new Location(player.getLocation()), player);
		player.getInventory().clear(slot);
		player.getCombat().reset();
		if (player.getInterfaceManager().getMain() != -1) {
			player.send(new SendRemoveInterfaces());
		}
		PlayerLogs.log(player.getUsername(),
				"Dropped Item (Id: " + drop + ", Amount: " + player.getInventory().getSlotAmount(drop.getAmount())
						+ ") at Position: " + player.getX() + ", " + player.getY() + ", " + player.getZ());
	}

	public boolean drop(Item item, Location location) {
		return GroundItemHandler.add(item, location, player);
	}

	public void dropFull(int id, int slot) {
		Item drop = player.getInventory().get(slot);
		GroundItemHandler.add(drop, new Location(player.getLocation()), player);
		player.getInventory().clear(slot);
	}

	private void load() {
		for (Iterator<?> g = GroundItemHandler.getActive().iterator(); g.hasNext();) {
			GroundItem i = (GroundItem) g.next();
			if (GroundItemHandler.visible(player, i)) {
				adding.add(i);
			}
		}
		hasLoaded = true;
	}

	public void onRegionChange() {
		hasLoaded = false;
		adding.clear();
		GroundItem g;
		while ((g = loaded.poll()) != null) {
			player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, g));
		}
	}

	public void pickup(final int x, final int y, final int id) {
		final GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(),
				player.getUsername(), false);
		if ((g == null) || (!GroundItemHandler.exists(g))) {
			player.getMovementHandler().reset();
			return;
		}
		TaskQueue.queue(new WalkToTask(player, g) {
			@Override
			public void onDestination() {
				GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(),
						player.getUsername(), false);
				if ((g == null) || (!g.exists())) {
					player.getMovementHandler().reset();
					stop();
					return;
				}
				if (!StraightPathFinder.isInteractionPathClear(player.getLocation(), g.getLocation())) {
					player.getClient().queueOutgoingPacket(new SendMessage("I can't reach that!"));
					stop();
					return;
				}
				if (IronMan.isIronMan(player) && g.getOwner() != player) {
					player.send(new SendMessage(
							"As an Iron man you are unable to pick up items that other players have dropped."));
					return;
				} else {
					if ((player.getInventory().hasSpaceFor(g.getItem())) && (GroundItemHandler.remove(g))) {
						player.getSkill().lock(1);
						SoundPlayer.play(player, Sounds.PICKUP_ITEM);
						player.getInventory().add(g.getItem());
						PlayerLogs.log(player.getUsername(),
								"Picked up an item (Id: " + g.getItem() + ", Amount: " + g.getItem().getAmount()
										+ ") at Position: " + player.getX() + ", " + player.getY() + ", "
										+ player.getZ());
					} else {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You do not have enough inventory space to pick that up."));
					}
				}
			}
		});
	}

	public void process() {
		if (!hasLoaded) {
			load();
		}
		GroundItem g = null;
		while ((g = adding.poll()) != null) {
			player.getClient().queueOutgoingPacket(new SendGroundItem(player, g));
			loaded.add(g);
		}
		while ((g = removing.poll()) != null)
			player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, g));
	}

	public void remove(GroundItem groundItem) {
		removing.add(groundItem);
	}

	public boolean stack(GroundItem g) {
		if (!g.getItem().getDefinition().isStackable()) {
			return false;
		}
		final GroundItem onGround = GroundItemHandler.getNonGlobalGroundItem(g.getItem().getId(),
				g.getLocation().getX(), g.getLocation().getY(), g.getLocation().getZ(), g.getLongOwnerName());
		if (onGround == null) {
			return false;
		}
		if (onGround.isGlobal()) {
			return false;
		}
		player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, onGround));
		onGround.getItem().add(g.getItem().getAmount());
		player.getClient().queueOutgoingPacket(new SendGroundItem(player, onGround));
		return true;
	}
}
