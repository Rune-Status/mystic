package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendEnergy extends OutgoingPacket {

	private final int energy;

	public SendEnergy(int energy) {
		super();
		this.energy = energy;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 110);
		out.writeByte(energy);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 110;
	}

}
