package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendAnimateObject extends OutgoingPacket {

	private final GameObject object;
	private final int animation;

	public SendAnimateObject(GameObject object, int animation) {
		this.object = object;
		this.animation = animation;
	}

	@Override
	public void execute(Client client) {
		if (object == null) {
			return;
		}
		new SendCoordinates(
				new Location(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ()),
				client.getPlayer()).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getType() << 2) + (object.getFace() & 3), StreamBuffer.ValueType.S);
		out.writeShort(animation, StreamBuffer.ValueType.A);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 160;
	}

}
