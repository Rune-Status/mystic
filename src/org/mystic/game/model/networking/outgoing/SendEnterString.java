package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.OutBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendEnterString extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		OutBuffer outBuffer = StreamBuffer.newOutBuffer(5);
		outBuffer.writeHeader(client.getEncryptor(), getOpcode());
		client.send(outBuffer.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 187;
	}

}
