package org.mystic.game.model.networking.packet.impl;

import org.mystic.cache.map.Region;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.WalkToActions;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.task.TaskQueue;

@SuppressWarnings("all")
public class ObjectInteractionPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isSpeared()) || (player.getMagic().isTeleporting()) || (player.isDead())
				|| (!player.getController().canClick())) {
			return;
		}
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		TaskQueue.onMovement(player);
		player.getCombat().reset();
		switch (opcode) {
		case 192:
			int interfaceId = in.readShort();
			int id = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			int y = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			int slot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			int x = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			int z = player.getLocation().getZ();
			int itemId = in.readShort();
			final Item interacted = player.getInventory().get(slot);
			if (interacted == null || itemId != interacted.getId() || !player.getInventory().playerHasItem(interacted)
					|| !player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			WalkToActions.itemOnObject(player, interacted.getId(), id, x, y);
			break;
		case 132:
			x = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			id = in.readShort();
			y = in.readShort(StreamBuffer.ValueType.A);
			z = player.getLocation().getZ();
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			WalkToActions.clickObject(player, 1, id, x, y);
			break;
		case 252:
			id = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			y = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			x = in.readShort(StreamBuffer.ValueType.A);
			z = player.getLocation().getZ();
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			WalkToActions.clickObject(player, 2, id, x, y);
			break;
		case 70:
			x = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			y = in.readShort();
			id = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			z = player.getLocation().getZ();
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			WalkToActions.clickObject(player, 3, id, x, y);
			break;
		case 234:
			x = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			id = in.readShort(StreamBuffer.ValueType.A);
			y = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			z = player.getLocation().getZ();
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			WalkToActions.clickObject(player, 4, id, x, y);
			break;
		case 35:
			x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			int magicId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.BIG);
			y = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.BIG);
			id = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			z = player.getLocation().getZ();
			if ((!Region.objectExists(id, x, y, z))
					&& (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
				System.out
						.println("Object found to be non-existent: id: " + id + " at x:" + x + "  y:" + y + "  z:" + z);
				return;
			}
			break;
		}
	}
}
