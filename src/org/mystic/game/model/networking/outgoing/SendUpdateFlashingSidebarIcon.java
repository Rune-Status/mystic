package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendUpdateFlashingSidebarIcon extends OutgoingPacket {

	private final int id;

	public SendUpdateFlashingSidebarIcon(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 152);
		out.writeByte(id);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 152;
	}

}
