package org.mystic.game.model.networking.packet.impl;

import org.mystic.Engine;
import org.mystic.game.World;
import org.mystic.game.model.content.clanchat.Clan;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.utility.Misc;

public class StringInputPacket extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		String input = Misc.longToPlayerName2(in.readLong());
		input = input.replaceAll("_", " ");
		if (player.getEnterXInterfaceId() == 55776) {
			player.setShopMotto(Misc.capitalizeFirstLetter(input));
			DialogueManager.sendStatement(player, "Player Owned Shops Exchange",
					"You have successfully changed your shop motto.", "Motto:",
					"@red@" + Misc.capitalizeFirstLetter(input), "");
			return;
		}
		if (player.getEnterXInterfaceId() == 55777) {
			player.getShopping().open(World.getPlayerByName(input));
			return;
		}
		if (player.getEnterXInterfaceId() == 55778) {
			player.getPlayerShop().setSearch(input);
			return;
		}
		if (player.getEnterXInterfaceId() == 6969) {
			if ((input != null) && (input.length() > 0) && (player.clan == null)) {
				Clan localClan = Engine.clanManager.getClan(input);
				if (localClan != null) {
					localClan.addMember(player);
				} else if (input.equalsIgnoreCase(player.getUsername())) {
					Engine.clanManager.create(player);
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage(Misc.formatPlayerName(input) + " has not setup their channel yet."));
				}
			}
		}
	}

}