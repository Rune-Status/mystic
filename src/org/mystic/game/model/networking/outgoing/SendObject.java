package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendObject extends OutgoingPacket {

	private final GameObject o;
	private final Location base;

	public SendObject(Player p, GameObject o) {
		super();
		this.o = o;
		this.base = new Location(p.getCurrentRegion());
	}

	@Override
	public void execute(Client client) {
		new SendCoordinates(o.getLocation(), base).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(o.getId(), StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(((o.getType() << 2) + (o.getFace() & 3)), StreamBuffer.ValueType.S);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 151;
	}

}
