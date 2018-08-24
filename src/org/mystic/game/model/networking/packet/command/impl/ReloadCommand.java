package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.model.entity.npc.drops.NPCDrops;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.Command;
import org.mystic.utility.GameDefinitionLoader;

public class ReloadCommand implements Command {

	public void load() {
		try {
			NPCDrops.parseDrops().load();
			GameDefinitionLoader.loadShopDefinitions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleCommand(Player player, String command) {
		load();
		player.send(new SendMessage("You have reloaded the drops and shops."));
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}

	@Override
	public Rights rightsRequired() {
		return Rights.OWNER;
	}
}
