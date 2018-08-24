package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendFlashSidebarIcon extends OutgoingPacket {

	private final int id;

	public SendFlashSidebarIcon(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 24);
		out.writeByte(-id, StreamBuffer.ValueType.A);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 24;
	}

}
