package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendCoordinates extends OutgoingPacket {

	private final Location p;
	private final Location base;

	public SendCoordinates(Location p, Location base) {
		super();
		this.p = p;
		this.base = base;
	}

	public SendCoordinates(Location p, Player player) {
		super();
		this.p = p;
		base = player.getCurrentRegion();
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 85);
		int y = p.getY() - 8 * base.getRegionY();
		int x = p.getX() - 8 * base.getRegionX();
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 85;
	}

}
