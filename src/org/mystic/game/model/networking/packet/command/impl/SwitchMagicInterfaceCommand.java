package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.model.content.skill.magic.MagicSkill;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendSidebarInterface;
import org.mystic.game.model.networking.packet.command.Command;

public class SwitchMagicInterfaceCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		if ((args[1] != null) && (args[1] != "")) {
			try {
				switch (Integer.parseInt(args[1])) {
				case 0:
					player.getMagic().setMagicBook(1151);
					break;
				case 1:
					player.getMagic().setMagicBook(12855);
					break;
				case 2:
					player.getMagic().setMagicBook(29999);
					break;
				default:
					player.getClient().queueOutgoingPacket(new SendMessage("syntax: ::switch or ::switch 0"));
				}
				return;
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(new SendMessage("syntax: ::switch or ::switch 0"));

				return;
			}
		}
		if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.MODERN)
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.ANCIENT);
		else if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.ANCIENT)
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.LUNAR);
		else if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.LUNAR) {
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.MODERN);
		}
		switch (player.getMagic().getSpellBookType().ordinal()) {
		case 0:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 1151));
			break;
		case 1:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 12855));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 29999));
		}
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
