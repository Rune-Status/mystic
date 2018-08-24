package org.mystic.game.model.content.trading;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendUpdateItems;

public class TradeContainer extends ItemContainer {

	private final Trade trade;

	public TradeContainer(Trade trade) {
		super(28, ItemContainer.ContainerTypes.STACK, true, true);
		this.trade = trade;
	}

	@Override
	public boolean allowZero(int id) {
		return false;
	}

	public void offer(int id, int amount, int slot) {
		if (!Item.getDefinition(id).isTradable()) {
			trade.getPlayer().getClient().queueOutgoingPacket(new SendMessage("This item is untradable."));
			return;
		}
		if (!trade.canAppendTrade()) {
			return;
		}
		if (!trade.getPlayer().getInventory().slotContainsItem(slot, id)) {
			return;
		}
		if ((trade.getStage() == Trade.TradeStages.STAGE_2)
				|| (trade.getStage() == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			return;
		}
		int invAmount = trade.getPlayer().getInventory().getItemAmount(id);
		if (invAmount < amount) {
			amount = invAmount;
		}
		int added = add(id, amount, true);
		if (added > 0) {
			trade.getPlayer().getInventory().remove(id, added);
		}
		trade.setStage(Trade.TradeStages.STAGE_1);
		trade.getTradingWith().setStage(Trade.TradeStages.STAGE_1);

		trade.getPlayer().getClient().queueOutgoingPacket(new SendString("", 3431));
		trade.getTradingWith().getPlayer().getClient().queueOutgoingPacket(new SendString("", 3431));
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
		if ((trade.getStage() == Trade.TradeStages.STAGE_2)
				|| (trade.getStage() == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			trade.getPlayer().getClient().queueOutgoingPacket(new SendUpdateItems(3322, null));
			trade.getTradingWith().getPlayer().getClient().queueOutgoingPacket(new SendUpdateItems(3322, null));
		} else {
			trade.getPlayer().getClient()
					.queueOutgoingPacket(new SendUpdateItems(3322, trade.getPlayer().getInventory().getItems()));
			trade.getTradingWith().getPlayer().getClient().queueOutgoingPacket(
					new SendUpdateItems(3322, trade.getTradingWith().getPlayer().getInventory().getItems()));
		}
		trade.getPlayer().getClient().queueOutgoingPacket(new SendUpdateItems(3415, trade.getTradedItems()));
		trade.getPlayer().getClient()
				.queueOutgoingPacket(new SendUpdateItems(3416, trade.getTradingWith().getTradedItems()));
		trade.getTradingWith().getPlayer().getClient()
				.queueOutgoingPacket(new SendUpdateItems(3415, trade.getTradingWith().getTradedItems()));
		trade.getTradingWith().getPlayer().getClient()
				.queueOutgoingPacket(new SendUpdateItems(3416, trade.getTradedItems()));
	}

	public void withdraw(int slot, int amount) {
		if (!trade.canAppendTrade()) {
			return;
		}
		if ((trade.getStage() == Trade.TradeStages.STAGE_2)
				|| (trade.getStage() == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			return;
		}
		if (!slotHasItem(slot)) {
			return;
		}
		int id = getSlotId(slot);
		int tradeAmount = getItemAmount(id);
		if (tradeAmount < amount) {
			amount = tradeAmount;
		}
		int removed = remove(id, amount);
		if (removed > 0) {
			trade.getPlayer().getInventory().add(id, removed);
		}
		trade.setStage(Trade.TradeStages.STAGE_1);
		trade.getTradingWith().setStage(Trade.TradeStages.STAGE_1);

		trade.getPlayer().getClient().queueOutgoingPacket(new SendString("", 3431));
		trade.getTradingWith().getPlayer().getClient().queueOutgoingPacket(new SendString("", 3431));
	}
}