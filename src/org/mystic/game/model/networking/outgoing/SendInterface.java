package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendInterface extends OutgoingPacket {

	private final int id;

	public SendInterface(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(client.getEncryptor(), 97);
		out.writeShort(id);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().setActive(id, -1);
	}

	@Override
	public int getOpcode() {
		return 97;
	}

}
