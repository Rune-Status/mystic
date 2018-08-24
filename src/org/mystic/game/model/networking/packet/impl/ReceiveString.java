package org.mystic.game.model.networking.packet.impl;

import org.mystic.Engine;
import org.mystic.game.model.content.clanchat.Clan;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.utility.Misc;

public class ReceiveString extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		final String text = in.readString();
		int index = text.indexOf(",");
		int id = Integer.parseInt(text.substring(0, index));
		String string = text.substring(index + 1);
		switch (id) {
		
		case 0:
			if (player.clan != null) {
				player.clan.removeMember(player);
				player.lastClanChat = "";
			} else {
				player.setEnterXInterfaceId(551);
			}
			break;

		case 1:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 15) {
				string = string.substring(0, 15);
			}
			Clan clan = player.getClan();
			if (clan == null) {
				Engine.clanManager.create(player);
				clan = player.getClan();
			}
			if (clan != null) {
				clan.setTitle(string);
				player.getClient().queueOutgoingPacket(new SendString(clan.getTitle(), 18306));
				clan.save();
			}
			break;

		case 2:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(player.getUsername())) {
				break;
			}
			clan = player.getClan();
			if (clan.isBanned(string)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You can't promote a banned member."));
				break;
			}
			if (clan != null) {
				clan.setRank(Misc.formatPlayerName(string), 1);
				player.setClanData();
				clan.save();
			}
			break;

		case 3:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(player.getUsername())) {
				break;
			}
			clan = player.getClan();
			if (clan.isRanked(string)) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You can't ban a ranked member of this clan chat channel."));
				break;
			}
			if (clan != null) {
				clan.banMember(Misc.formatPlayerName(string));
				player.setClanData();
				clan.save();
			}
			break;

		}
	}
}