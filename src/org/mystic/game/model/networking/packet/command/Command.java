package org.mystic.game.model.networking.packet.command;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;

public abstract interface Command {

	public abstract void handleCommand(Player paramPlayer, String paramString);

	public abstract boolean meetsRequirements(Player paramPlayer);

	public abstract Rights rightsRequired();
}
