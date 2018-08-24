package org.mystic.game.model.networking.packet.impl;

import org.mystic.game.model.content.clanchat.Clan;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.packet.IncomingPacket;
import org.mystic.utility.Misc;

public class InterfaceAction extends IncomingPacket {

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		final int id = in.readShort(false);
		final int action = in.readShort(false);
		if ((player.isDead()) || (player.getMagic().isTeleporting()) || (!player.getController().canClick())) {
			return;
		}
		final Clan clan = player.getClan();
		switch (id) {

		case 18304:
			if (action == 1) {
				player.getClan().delete();
				player.setClanData();
			}
			break;

		case 18307:
		case 18310:
		case 18313:
		case 18316:
			if (clan != null) {
				if (id == 18307) {
					clan.setRankCanJoin(action == 0 ? -1 : action);
				} else if (id == 18310) {
					clan.setRankCanTalk(action == 0 ? -1 : action);
				} else if (id == 18313) {
					clan.setRankCanKick(action == 0 ? -1 : action);
				} else if (id == 18316) {
					clan.setRankCanBan(action == 0 ? -1 : action);
				}
				String title = "";
				if (id == 18307) {
					title = clan.getRankTitle(clan.whoCanJoin)
							+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18310) {
					title = clan.getRankTitle(clan.whoCanTalk)
							+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18313) {
					title = clan.getRankTitle(clan.whoCanKick)
							+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
				} else if (id == 18316) {
					title = clan.getRankTitle(clan.whoCanBan)
							+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
				}
				player.getClient().queueOutgoingPacket(new SendString(title, id + 2));
			}
			break;
		}
		if (id >= 18323 && id < 18423) {
			if (clan != null && clan.rankedMembers != null && !clan.rankedMembers.isEmpty()) {
				String member = clan.rankedMembers.get(id - 18323);
				switch (action) {
				case 0:
					clan.demote(member);
					break;
				default:
					clan.setRank(member, action);
					break;
				}
				player.setClanData();
			}
		}
		if (id >= 18424 && id < 18524) {
			if (clan != null && clan.bannedMembers != null && !clan.bannedMembers.isEmpty()) {
				String member = clan.bannedMembers.get(id - 18424);
				switch (action) {
				case 0:
					clan.unbanMember(member);
					break;
				}
				player.setClanData();
			}
		}
		if (id >= 18144 && id < 18244) {
			for (int index = 0; index < 100; index++) {
				if (id == index + 18144) {
					String member = player.clan.activeMembers.get(id - 18144);
					if (member == null) {
						return;
					}
					switch (action) {
					case 0:
						if (player.clan.isFounder(player.getUsername()) && !player.getCombat().inCombat()) {
							player.getClient().queueOutgoingPacket(new SendInterface(18300));
						}
						break;
					case 1:
						if (member.equalsIgnoreCase(player.getUsername())) {
							player.getClient().queueOutgoingPacket(new SendMessage("You can't kick yourself!"));
						} else {
							if (player.clan.canKick(player.getUsername())) {
								player.clan.kickMember(member);
							} else {
								player.getClient().queueOutgoingPacket(
										new SendMessage("You do not have sufficient privileges to do this."));
							}
						}
						break;
					case 2:
						if (member.length() == 0) {
							break;
						} else if (member.length() > 12) {
							member = member.substring(0, 12);
						}
						if (member.equalsIgnoreCase(player.getUsername())) {
							break;
						}
						if (clan.isRanked(member) || PlayerConstants.isOwner(member)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("You can't ban a ranked member of this channel."));
							break;
						}
						if (clan != null) {
							clan.banMember(Misc.formatPlayerName(member));
							player.setClanData();
							clan.save();
						}
						break;
					}
					break;
				}
			}
		}
	}
}