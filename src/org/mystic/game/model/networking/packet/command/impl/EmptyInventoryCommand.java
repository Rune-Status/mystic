package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.packet.command.Command;

public class EmptyInventoryCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		player.getInventory().clear();
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}

	@Override
	public Rights rightsRequired() {
		return Rights.ADMINISTRATOR;
	}
}
