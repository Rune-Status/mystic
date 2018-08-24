package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.ByteOrder;
import org.mystic.game.model.networking.StreamBuffer.ValueType;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendColor extends OutgoingPacket {

	private final int id;

	private final int color;

	public SendColor(int id, int color) {
		super();
		this.id = id;
		this.color = color;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8);
		out.writeHeader(client.getEncryptor(), 122);
		out.writeShort(id, ValueType.A, ByteOrder.LITTLE);
		out.writeShort(color, ValueType.A, ByteOrder.LITTLE);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 122;
	}

}
