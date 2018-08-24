package org.mystic.game.model.content;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;

/**
 * Manages the Runepouch item functionality
 * 
 * @author Vali - http://www.rune-server.org/members/Valli
 *
 */
public class RunePouch extends ItemContainer {

	private final Player player;

	public RunePouch(Player player, int size) {
		super(size, ItemContainer.ContainerTypes.STACK, true, true);
		this.player = player;
	}

	@Override
	public boolean allowZero(int paramInt) {
		return false;
	}

	@Override
	public void onAdd(Item paramItem) {
	}

	@Override
	public void onFillContainer() {

	}

	@Override
	public void onMaxStack() {

	}

	@Override
	public void onRemove(Item paramItem) {
	}

	private final Item[] items = { new Item(1, 1), new Item(1, 1), new Item(1, 1), };

	@Override
	public void update() {
		if (player.getInventory().playerHasItem(new Item(20085))) {
			player.getClient().queueOutgoingPacket(new SendUpdateItems(41710, getItems()));
			player.getClient().queueOutgoingPacket(new SendUpdateItems(41711, player.getInventory().getItems()));
		} else {
			player.getClient().queueOutgoingPacket(new SendUpdateItems(41710, items));
		}
	}

	public void open() {
		if (player.isBusy() || player.getCombat().inCombat() || player.isDead() || player.getMagic().isTeleporting()) {
			player.send(new SendMessage("Please finish what you are doing before opening the rune pouch."));
			return;
		}
		update();
		player.getClient().queueOutgoingPacket(new SendInterface(41700));
	}

	public void store(Item item) {
		if (item != null) {
			store(item.getId(), item.getAmount());
		}
	}

	public void store(int id, int amount) {
		if (!Item.getDefinition(id).isStackable() || !Item.getDefinition(id).isTradable()
				|| !Item.getDefinition(id).getName().endsWith("rune")) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your pouch seems to only be able to fit runes."));
			return;
		}
		if (hasSpaceFor(new Item(id, amount))) {
			if (player.getInventory().getItemAmount(id) < amount) {
				amount = player.getInventory().getItemAmount(id);
			}
			int slot = getItemSlot(id);
			Item item = new Item(id, amount);
			if (slot == -1 || !item.getDefinition().isStackable()) {
				for (int i = 0; i < getSize(); i++) {
					if (getItems()[i] == null) {
						slot = i;
						break;
					}
				}
			}
			add(item, true);
			player.getInventory().remove(new Item(id, amount), false);
			player.getInventory().update();
			update();
		} else {
			player.send(new SendMessage("Your rune pouch is full."));
		}
	}

	public void withdraw(int itemId, int slot, int amount) {
		Item item = get(slot);
		if (item == null || item.getId() != itemId) {
			return;
		}
		if (player.getInventory().hasSpaceFor(item)) {
			amount = removeFromSlot(slot, itemId, amount);
			if (amount <= 0) {
				return;
			}
			player.getInventory().add(new Item(itemId, amount));
			shift();
			update();
		} else {
			player.send(new SendMessage("You do not have enough free inventory spaces to empty your pouch."));
		}
	}

	public void withdrawAll() {
		if (getFreeSlots() == 3) {
			player.send(new SendMessage("Your pouch is empty."));
			return;
		}
		if (player.getInventory().getFreeSlots() >= getSize()) {
			for (int i = 0; i < getSize(); i++) {
				Item item = get(i);
				if (item == null) {
					continue;
				}
				player.getInventory().add(item);
			}
			clear();
			update();
			player.send(new SendMessage("You empty out your pouch."));
		} else {
			player.send(new SendMessage("You do not have enough free inventory spaces to withdraw from your pouch."));
		}
	}
}