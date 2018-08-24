package org.mystic.game.model.entity.player;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class Inventory extends ItemContainer {

	private final Player player;
	
	public static final int INTERFACE_ID = 3214;

	public Inventory(Player player) {
		super(28, ItemContainer.ContainerTypes.STACK, false, true);
		this.player = player;
	}

	public void addOnLogin(Item item, int slot) {
		if (item == null) {
			return;
		}
		getItems()[slot] = item;
		onAdd(item);
	}

	public void addOrCreateGroundItem(int id, int amount, boolean update) {
		if (player.getInventory().hasSpaceFor(new Item(id, amount))) {
			player.getInventory().insert(id, amount);
		} else if ((amount > 1) && (!Item.getDefinition(id).isStackable())) {
			for (int i = 0; i < amount; i++)
				GroundItemHandler.add(new Item(id, 1), player.getLocation(), player);
		} else {
			GroundItemHandler.add(new Item(id, amount), player.getLocation(), player);
		}
		if (update) {
			update();
		}
	}

	@Override
	public boolean allowZero(int id) {
		return false;
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onFillContainer() {
		player.getClient()
				.queueOutgoingPacket(new SendMessage("You do not have enough inventory space to carry that."));
	}

	@Override
	public void onMaxStack() {
		player.getClient().queueOutgoingPacket(new SendMessage("You won't be able to carry all that!"));
	}

	@Override
	public void onRemove(Item item) {

	}

	@Override
	public void setItems(Item[] items) {
		super.setItems(items);
		update();
	}

	@Override
	public void update() {
		player.getRunePouch().update();
		player.getSummoning().onUpdateInventory();
		player.updateItems = true;
	}

}