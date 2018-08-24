package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendSystemBan extends OutgoingPacket {

	public SendSystemBan() {
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(client.getEncryptor(), 1);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 1;
	}

}
