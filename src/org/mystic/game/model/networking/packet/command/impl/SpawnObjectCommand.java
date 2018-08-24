package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.Command;

public class SpawnObjectCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		player.send(new SendMessage("Object (" + Integer.parseInt(args[1]) + ") has been spawned."));
		ObjectManager.register(new GameObject(Integer.parseInt(args[1]), player.getLocation().getX(),
				player.getLocation().getY(), player.getLocation().getZ(), 10, 0));
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
