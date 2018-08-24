package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendLogout extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(client.getEncryptor(), 109);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 109;
	}

}
