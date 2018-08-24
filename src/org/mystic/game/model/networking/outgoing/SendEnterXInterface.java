package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.OutBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendEnterXInterface extends OutgoingPacket {

	private final int interfaceId;
	private final int itemId;

	public SendEnterXInterface() {
		interfaceId = 0;
		itemId = 0;
	}

	public SendEnterXInterface(int interfaceId, int itemId) {
		this.interfaceId = interfaceId;
		this.itemId = itemId;
	}

	@Override
	public void execute(Client client) {
		if (itemId > 0 || interfaceId > 0) {
			client.getPlayer().setEnterXInterfaceId(interfaceId);
			client.getPlayer().setEnterXItemId(itemId);
		}
		OutBuffer outBuffer = StreamBuffer.newOutBuffer(5);
		outBuffer.writeHeader(client.getEncryptor(), getOpcode());
		client.send(outBuffer.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 27;
	}

}
