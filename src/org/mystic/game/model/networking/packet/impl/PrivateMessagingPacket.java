package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.packet.IncomingPacket;

public class PrivateMessagingPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		switch (opcode) {

		case 188:
			long name = in.readLong();
			if (name == player.getUsernameToLong()) {
				return;
			}
			player.getPrivateMessaging().addFriend(name);
			break;

		case 215:
			name = in.readLong();
			player.getPrivateMessaging().removeFriend(name);
			break;

		case 133:
			name = in.readLong();
			player.getPrivateMessaging().addIgnore(name);
			break;

		case 74:
			name = in.readLong();
			player.getPrivateMessaging().removeIgnore(name);
			break;

		case 126:
			name = in.readLong();
			int size = length - 8;
			byte[] message = in.readBytes(size);
			player.getPrivateMessaging().sendPrivateMessage(name, size, message);
			break;

		}
	}
}
