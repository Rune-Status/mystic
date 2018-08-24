package org.mystic.game.model.networking;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;

public class ClientMap {

	public static boolean allow(Client client) {
		byte am = 0;
		for (Player p : World.getPlayers()) {
			if (p != null && p.getClient().getHost() != null && p.getClient().getHost().equals(client.getHost())) {
				am++;
			}
		}
		return am < 2;
	}

	private ClientMap() {

	}

}
