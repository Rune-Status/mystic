package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendDetails;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class ChangeRegionPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		player.getClient().queueOutgoingPacket(new SendDetails(player.getIndex()));

		player.getGroundItems().onRegionChange();
		player.getObjects().onRegionChange();

		if (player.getDueling().isStaking()) {
			player.getDueling().decline();
		}

		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}

		player.resetAggression();
	}
}
