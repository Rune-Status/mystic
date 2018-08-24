package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendShakeScreen extends OutgoingPacket {

	private final boolean shake;

	public SendShakeScreen(boolean shake) {
		this.shake = shake;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 35);
		if (shake) {
			out.writeByte(4);
			out.writeByte(4);
			out.writeByte(4);
			out.writeByte(4);
		} else {
			out.writeByte(1);
			out.writeByte(0);
			out.writeByte(0);
			out.writeByte(0);
		}
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 35;
	}
}
