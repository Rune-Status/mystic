package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendUpdateExperience extends OutgoingPacket {

	private final int id;

	private final int exp;

	private final int exp2;

	public SendUpdateExperience(int id, int exp) {
		super();
		this.id = id;
		this.exp = exp;
		this.exp2 = exp;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8);
		out.writeHeader(client.getEncryptor(), 133);
		out.writeByte(id);
		out.writeInt(exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(exp2);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 133;
	}
}
