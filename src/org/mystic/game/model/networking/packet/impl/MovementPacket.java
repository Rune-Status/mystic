package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.pathfinding.RS317PathFinder;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.task.TaskQueue;

public class MovementPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 2;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (player.getMagic().isTeleporting()) || (!player.getController().canMove(player))) {
			player.getCombat().reset();
			return;
		}
		if (player.getAttributes().get("stopMovement") != null) {
			if ((boolean) player.getAttributes().get("stopMovement")) {
				return;
			}
		}
		if (player.getDialogue() != null) {
			player.getDialogue().end();
			player.send(new SendRemoveInterfaces());
		}
		if (player.isDoingCape()) {
			return;
		}
		if (player.isSpeared()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't move."));
			player.getCombat().reset();
			return;
		}
		if (player.isFrozen()) {
			player.getCombat().reset();
			player.getClient().queueOutgoingPacket(new SendMessage("A magical force stops you from moving."));
			return;
		}
		if (opcode == 248) {
			length -= 14;
		}
		if (opcode != 98) {
			player.getMovementHandler().setForced(false);
			player.getMagic().getSpellCasting().disableClickCast();
			player.getFollowing().reset();
			player.getCombat().reset();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			if (player.getTrade().trading()) {
				player.getTrade().end(false);
			}
			if (player.getDueling().isStaking()) {
				player.getDueling().decline();
			}
			player.getShopping().reset();
			player.getInterfaceManager().reset();
			TaskQueue.onMovement(player);
			player.setEnterXInterfaceId(0);
		} else {
			player.getMovementHandler().setForced(true);
		}
		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		for (int i = 0; i < steps; i++) {
			path[i][0] = in.readByte();
			path[i][1] = in.readByte();
		}
		int firstStepY = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		in.readByte(StreamBuffer.ValueType.C);
		player.getMovementHandler().reset();
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
		}
		if (steps > 0) {
			if ((Math.abs(path[(steps - 1)][0] - player.getLocation().getX()) > 21)
					|| (Math.abs(path[(steps - 1)][1] - player.getLocation().getY()) > 21)) {
				player.getMovementHandler().reset();
			}
		} else if ((Math.abs(firstStepX - player.getLocation().getX()) > 21)
				|| (Math.abs(firstStepY - player.getLocation().getY()) > 21)) {
			player.getMovementHandler().reset();
			return;
		}
		if (steps > 0) {
			RS317PathFinder.findRoute(player, path[(steps - 1)][0], path[(steps - 1)][1], true, 16, 16);
		} else {
			RS317PathFinder.findRoute(player, firstStepX, firstStepY, true, 16, 16);
		}
	}
	
}