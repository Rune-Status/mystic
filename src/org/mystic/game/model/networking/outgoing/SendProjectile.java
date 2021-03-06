package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendProjectile extends OutgoingPacket {

	private final Projectile p;

	private final int lock;

	private final byte offsetX;

	private final byte offsetY;

	private final SendAltCoordinates sendAlt;

	public SendProjectile(Player player, Projectile p, Location pos, int lock, byte offsetX, byte offsetY) {
		super();
		this.p = p;
		this.lock = lock;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		if (p.getSize() > 1) {
			sendAlt = new SendAltCoordinates(
					new Location(pos.getX() + (p.getSize() / 2), pos.getY() + (p.getSize() / 2)), player);
		} else {
			sendAlt = new SendAltCoordinates(pos, player);
		}
	}

	@Override
	public void execute(Client client) {
		sendAlt.execute(client);

		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(client.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetX);
		out.writeByte(offsetY);
		out.writeShort(lock);
		out.writeShort(p.getId());
		out.writeByte(p.getStartHeight());
		out.writeByte(p.getEndHeight());
		out.writeShort(p.getDelay());
		out.writeShort(p.getDuration());
		out.writeByte(p.getCurve());
		out.writeByte(64);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 117;
	}

}
