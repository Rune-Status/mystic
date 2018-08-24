package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.impl.GroundItem;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendRemoveGroundItem extends OutgoingPacket {

	private final GroundItem g;
	private final Location pRegion;

	public SendRemoveGroundItem(Player p, GroundItem g) {
		super();
		this.g = g;
		pRegion = new Location(p.getCurrentRegion());
	}

	@Override
	public void execute(Client client) {
		new SendCoordinates(g.getLocation(), pRegion).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(client.getEncryptor(), 156);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(g.getItem().getId());
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 156;
	}

}
