package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendLoginResponse extends OutgoingPacket {

	private final int response;

	private final Rights rights;

	public SendLoginResponse(int response, Rights rights) {
		super();
		this.response = response;
		this.rights = rights;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
		resp.writeByte(response);
		resp.writeByte(rights.getProtocolValue());
		resp.writeByte(0);
		client.send(resp.getBuffer());
		new SendMapRegion(client.getPlayer()).execute(client);
		new SendDetails(client.getPlayer().getIndex()).execute(client);
	}

	@Override
	public int getOpcode() {
		return -1;
	}

}