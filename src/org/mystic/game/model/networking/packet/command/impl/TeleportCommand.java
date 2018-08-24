package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.Command;

public class TeleportCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		switch (args[0]) {

		case "goto":
			String username = command.substring(5);
			for (Player players : World.getPlayers()) {
				if (players != null) {
					if (players.getUsername().equalsIgnoreCase(username)) {
						player.getMagic().teleportNoWildernessRequirement(players.getLocation().getX(),
								players.getLocation().getY(), players.getLocation().getZ(), TeleportTypes.SPELL_BOOK);
						player.send(new SendMessage("You have been summoned to: " + players.getUsername()));
					}
				}
			}
			break;

		case "summon":
			String user = command.substring(7);
			for (Player players : World.getPlayers()) {
				if (players != null) {
					if (players.getUsername().equalsIgnoreCase(user)) {
						players.getMagic().teleportNoWildernessRequirement(player.getLocation().getX(),
								player.getLocation().getY(), player.getLocation().getZ(), TeleportTypes.SPELL_BOOK);
						players.send(new SendMessage("You have been summoned by: " + player.getUsername()));
					}
				}
			}
			break;
		}
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