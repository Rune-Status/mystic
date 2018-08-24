package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.ByteOrder;
import org.mystic.game.model.networking.StreamBuffer.ValueType;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendClanChatUpdate extends OutgoingPacket {

	private final long[] names;
	private final byte[] ranks;

	public SendClanChatUpdate(Player[] inClan, long owner) {
		ranks = new byte[100];
		names = new long[100];

		for (int i = 0; i < 100; i++) {
			ranks[i] = -1;
		}

		int index = 0;
		for (Player i : inClan) {
			if (i != null) {
				names[index] = i.getUsernameToLong();
				ranks[index] = (owner == i.getUsernameToLong() ? (byte) 3 : (byte) 1);
				index++;
			}
		}
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer((100 * 8) + 100 + 5);
		out.writeHeader(client.getEncryptor(), 1);

		for (int i = 0; i < names.length; i++) {
			out.writeLong(names[i], ValueType.STANDARD, ByteOrder.LITTLE);
			out.writeByte(ranks[i]);
		}

		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 2;
	}

}
