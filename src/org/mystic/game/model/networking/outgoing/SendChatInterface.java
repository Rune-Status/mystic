package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendChatInterface extends OutgoingPacket {

	private final int id;

	public SendChatInterface(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 164);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().setChat(id);
	}

	@Override
	public int getOpcode() {
		return 164;
	}

}
