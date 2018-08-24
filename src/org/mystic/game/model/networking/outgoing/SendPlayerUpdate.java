package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.player.PlayerUpdateFlags;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.PlayerUpdating;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendPlayerUpdate extends OutgoingPacket {

	private final PlayerUpdateFlags[] pFlags;

	public SendPlayerUpdate(PlayerUpdateFlags[] pFlags) {
		super();
		this.pFlags = pFlags;
	}

	@Override
	public void execute(Client client) {
		PlayerUpdating.update(client.getPlayer(), pFlags);
	}

	@Override
	public int getOpcode() {
		return 81;
	}

}
