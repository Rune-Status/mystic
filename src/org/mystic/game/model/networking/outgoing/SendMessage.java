package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendMessage extends OutgoingPacket {

	private final String message;

	public SendMessage(String message) {
		super();
		this.message = message;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 3);
		out.writeVariablePacketHeader(client.getEncryptor(), 253);
		out.writeString(message);
		out.finishVariablePacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 253;
	}

}
