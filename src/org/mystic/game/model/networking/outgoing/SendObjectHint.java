package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendObjectHint extends OutgoingPacket {

	private final Location p;

	private final int pos;

	public SendObjectHint(Location p, int pos) {
		super();
		this.p = p;
		this.pos = pos;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 254);
		out.writeByte(pos);
		out.writeShort(p.getX());
		out.writeShort(p.getY());
		out.writeByte(p.getZ());
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 254;
	}

}
