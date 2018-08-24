package org.mystic.game.model.content.minigames.duelarena;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;

public class StakingContainer extends ItemContainer {

	private final Player player;

	public StakingContainer(Player player) {
		super(28, ItemContainer.ContainerTypes.STACK, true, true);
		this.player = player;
	}

	@Override
	public boolean allowZero(int id) {
		return false;
	}

	public void offer(int id, int amount, int slot) {
		if (!player.getDueling().canAppendStake()) {
			return;
		}
		if (!Item.getDefinition(id).isTradable()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't stake this item."));
			return;
		}
		int invAmount = player.getInventory().getItemAmount(id);
		if (invAmount == 0) {
			return;
		}
		if (invAmount < amount) {
			amount = invAmount;
		}
		int removed = player.getInventory().remove(new Item(id, amount));
		if (removed > 0) {
			add(id, removed);
			withdraw(getItemSlot(995), -(amount));
		} else {
			return;
		}
		player.getDueling().onStake();
		player.getDueling().getInteracting().getDueling().onStake();
		update();
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onFillContainer() {
	}

	@Override
	public void onMaxStack() {
	}

	@Override
	public void onRemove(Item item) {
	}

	@Override
	public void update() {
		player.getClient().queueOutgoingPacket(new SendUpdateItems(6669, getItems()));
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3322, player.getInventory().getItems()));
		if (player.getDueling().getInteracting() != null) {
			player.getDueling().getInteracting().getClient().queueOutgoingPacket(new SendUpdateItems(6670, getItems()));
		}
	}

	public void withdraw(int slot, int amount) {
		if ((get(slot) == null) || (!player.getDueling().canAppendStake())) {
			return;
		}
		final int id = get(slot).getId();
		int removed = remove(id, amount);
		if (removed > 0)
			player.getInventory().add(new Item(id, removed));
		else {
			return;
		}
		player.getDueling().onStake();
		player.getDueling().getInteracting().getDueling().onStake();
		update();
	}

}