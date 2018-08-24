package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendRemoveInterfaces extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 219);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().reset();
		client.getPlayer().getAttributes().remove("setapp");
	}

	@Override
	public int getOpcode() {
		return 219;
	}

}
