package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendSkill extends OutgoingPacket {

	private final int id;

	private final int level;

	private final int exp;

	public SendSkill(int id, int level, int exp) {
		super();
		this.id = id;
		this.level = level;
		this.exp = exp;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8);
		out.writeHeader(client.getEncryptor(), 134);
		out.writeByte(id);
		out.writeInt(exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(level);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 134;
	}

}
