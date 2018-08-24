package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendStillCamera extends OutgoingPacket {

	private final Location p;

	private final int speed;

	private final int angle;

	public SendStillCamera(Location p, int speed, int angle) {
		super();
		this.p = p;
		this.speed = speed;
		this.angle = angle;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 177);
		out.writeByte(p.getX() / 64);
		out.writeByte(p.getY() / 64);
		out.writeShort(p.getZ());
		out.writeByte(speed);
		out.writeByte(angle);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 177;
	}

}
