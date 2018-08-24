package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendMapRegion extends OutgoingPacket {

	private final Location p;

	public SendMapRegion(Player player) {
		super();
		player.getCurrentRegion().setAs(player.getLocation());
		p = player.getLocation();
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 73);
		out.writeShort(p.getRegionX() + 6, StreamBuffer.ValueType.A);
		out.writeShort(p.getRegionY() + 6);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 73;
	}

}
