package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.Command;
import org.mystic.utility.Misc;

public class PlayersOnlineCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		player.send(new SendMessage(
				"There are currently @dre@" + Misc.format(World.getActivePlayers()) + "</col> players online."));
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}

	@Override
	public Rights rightsRequired() {
		return Rights.PLAYER;
	}
}
