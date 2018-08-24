package org.mystic.game.model.networking.outgoing;

import org.mystic.game.model.entity.npc.NpcUpdateFlags;
import org.mystic.game.model.entity.player.PlayerUpdateFlags;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.NPCUpdating;
import org.mystic.game.model.networking.out.OutgoingPacket;

public class SendNPCUpdate extends OutgoingPacket {

	private final NpcUpdateFlags[] nFlags;
	private final PlayerUpdateFlags pFlags;

	public SendNPCUpdate(NpcUpdateFlags[] nFlags, PlayerUpdateFlags pFlags) {
		super();
		this.nFlags = nFlags;
		this.pFlags = pFlags;
	}

	@Override
	public void execute(Client client) {
		NPCUpdating.update(client, pFlags, nFlags);
	}

	@Override
	public int getOpcode() {
		return 65;
	}

}
