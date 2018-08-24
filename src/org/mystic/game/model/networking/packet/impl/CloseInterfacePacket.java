package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class CloseInterfacePacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		player.getInterfaceManager().reset();
		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}
		if (player.getDueling().isStaking()) {
			player.getDueling().decline();
		}
		player.getShopping().reset();
	}
}
