package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.StreamBuffer.OutBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendClanMessage extends OutgoingPacket {

	private final String username;

	private final String message;

	private final String clanOwner;

	private final int rights;

	public SendClanMessage(String username, String message, String owner, int rights) {
		this.username = username;
		this.message = message;
		this.clanOwner = owner;
		this.rights = rights;
	}

	@Override
	public void execute(Client client) {
		OutBuffer out = StreamBuffer.newOutBuffer(100);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeString(username);
		out.writeString(message);
		out.writeString(clanOwner);
		out.writeShort(rights & 0xFF);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 217;
	}

}
