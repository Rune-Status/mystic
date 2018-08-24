package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendSidebarInterface extends OutgoingPacket {

	private final int tabId;

	private final int interfaceId;

	public SendSidebarInterface(int tabId, int interfaceId) {
		super();
		this.tabId = tabId;
		this.interfaceId = interfaceId;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(client.getEncryptor(), 71);
		out.writeShort(interfaceId);
		out.writeByte(tabId, StreamBuffer.ValueType.A);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().setTabId(tabId, interfaceId);
	}

	@Override
	public int getOpcode() {
		return 71;
	}

}
