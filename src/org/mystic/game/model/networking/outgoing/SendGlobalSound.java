package org.mystic.game.model.networking.outgoing;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;
import org.mystic.utility.Misc;

public class SendGlobalSound extends OutgoingPacket {

	private final int id;

	private final int type;

	private final int delay;

	public SendGlobalSound(int id, int type, int delay) {
		super();
		this.id = id;
		this.type = type;
		this.delay = delay;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(18);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeShort(id);
		out.writeByte(type);
		out.writeShort(delay);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				if (Misc.getExactDistance(client.getPlayer().getLocation(), player.getLocation()) < 10) {
					player.getClient().send(out.getBuffer());
				}
			}
		}
	}

	@Override
	public int getOpcode() {
		return 174;
	}

}
