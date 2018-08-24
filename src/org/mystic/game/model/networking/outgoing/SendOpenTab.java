package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendOpenTab extends OutgoingPacket {

	private final int id;

	public SendOpenTab(int id) {
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 106);
		out.writeByte(id, StreamBuffer.ValueType.C);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 106;
	}

}
