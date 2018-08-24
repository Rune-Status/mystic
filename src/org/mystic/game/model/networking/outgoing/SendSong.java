package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendSong extends OutgoingPacket {

	private final int id;

	public SendSong(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		if (id != -1) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
			out.writeHeader(client.getEncryptor(), 74);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			client.send(out.getBuffer());
		}
	}

	@Override
	public int getOpcode() {
		return 74;
	}

}
