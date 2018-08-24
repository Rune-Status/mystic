package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendStillGraphic extends OutgoingPacket {

	private final int id;

	private final Location p;

	private final int delay;

	public SendStillGraphic(int id, Location p, int delay) {
		super();
		this.id = id;
		this.p = p;
		this.delay = delay;
	}

	@Override
	public void execute(Client client) {
		new SendCoordinates(p, client.getPlayer()).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 4);
		out.writeByte(0);
		out.writeShort(id);
		out.writeByte(p.getZ());
		out.writeShort(delay);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 4;
	}

}
