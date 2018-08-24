package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.content.skill.cooking.CookingTask;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendEnterXInterface;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;

@SuppressWarnings("all")
public class ChatInterfacePacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 10;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		switch (opcode) {
		case 40:
			handleDialogue(player);
			break;
		case 135:
			showEnterX(player, in);
			break;
		case 208:
			handleEnterX(player, in);
			break;
		}
	}

	public void handleDialogue(Player player) {
		if ((player.getDialogue() == null) || (player.getDialogue().getNext() == -1)) {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		} else if (player.getDialogue().getNext() > -1) {
			player.getDialogue().execute();
		}
	}

	public void handleEnterX(Player player, StreamBuffer.InBuffer in) {
		int amount = in.readInt();
		int slot = player.getEnterXSlot();
		int id = player.getEnterXItemId();
		if (amount < 1) {
			return;
		}
		switch (player.getEnterXInterfaceId()) {
		case 15460:
			player.getPlayerShop().onSetPrice(player, amount);
			break;
		case 2700:
			if (player.getSummoning().isFamilarBOB()) {
				player.getSummoning().getContainer().withdraw(slot, amount);
			}
			break;
		case 3823:
			player.getShopping().sell(id, amount, slot);
			break;
		case 3900:
			player.getShopping().buy(id, amount, slot);
			break;
		case 1743:
			CookingTask.attemptCooking(player, player.getAttributes().getInt("cookingitem"),
					player.getAttributes().getInt("cookingobject"), amount);
			break;
		case 5064:
			if (!player.getInventory().slotContainsItem(slot, id)) {
				return;
			}
			if (player.getInterfaceManager().hasBankOpen()) {
				player.getBank().deposit(id, amount, slot);
			} else if (player.getSummoning().isFamilarBOB()) {
				player.getSummoning().getContainer().store(id, amount, slot);
			}
			break;
		case 5382:
			if (amount == -1 || amount < 0) {
				amount = player.getBank().get(slot).getAmount();
			}
			player.getBank().withdraw(slot, amount);
			break;
		case 6669:
			if (player.getDueling().isStaking()) {
				player.getDueling().getContainer().withdraw(slot, amount);
			}
			break;
		case 3322:
			if (player.getTrade().trading())
				player.getTrade().getContainer().offer(id, amount, slot);
			else if (player.getDueling().isStaking()) {
				player.getDueling().getContainer().offer(id, amount, slot);
			}
			break;
		case 3415:
			if (player.getTrade().trading()) {
				player.getTrade().getContainer().withdraw(slot, amount);
			}
			break;
		}
	}

	public void showEnterX(Player player, StreamBuffer.InBuffer in) {
		player.setEnterXSlot(in.readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setEnterXInterfaceId(in.readShort(StreamBuffer.ValueType.A));
		player.setEnterXItemId(in.readShort(StreamBuffer.ByteOrder.LITTLE));
		player.getClient().queueOutgoingPacket(new SendEnterXInterface());
	}
}