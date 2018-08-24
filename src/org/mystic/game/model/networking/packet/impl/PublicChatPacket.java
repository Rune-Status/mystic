package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.utility.Misc;

public class PublicChatPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		final int effects = in.readByte(false, StreamBuffer.ValueType.S);
		final int color = in.readByte(false, StreamBuffer.ValueType.S);
		final int chatLength = length - 2;
		byte[] text = in.readBytesReverse(chatLength, StreamBuffer.ValueType.A);
		if (!player.getController().canTalk()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't talk right now."));
			return;
		}
		final String message = Misc.textUnpack(text, chatLength);
		if (message.length() > 60) {
			return;
		}
		if (message.contains("@cr")) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Your message contained a blocked character and was not published globally."));
			return;
		}
		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);
		if (player.isMuted()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You are muted. You will be unmuted in " + player.getRemainingMute() + " days."));
		} else {
			player.setChatUpdateRequired(true);
		}
		if (!PlayerConstants.isOwner(player)) {
			PlayerLogs.log("SEVER", player.getUsername() + ": " + message);
		}
	}
}
