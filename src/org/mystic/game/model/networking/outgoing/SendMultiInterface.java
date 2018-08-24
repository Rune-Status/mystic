package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendMultiInterface extends OutgoingPacket {

	private final boolean multi;

	public SendMultiInterface(boolean multi) {
		super();
		this.multi = multi;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 61);
		out.writeByte(multi ? 1 : 0);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 61;
	}

}
