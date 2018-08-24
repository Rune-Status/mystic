package org.mystic.game.model.content.clanchat;

import java.util.Collections;
import java.util.LinkedList;

import org.mystic.Engine;
import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.utility.Misc;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Clan {

	public static class Rank {

		public static final int ANYONE = -1;

		public static final int FRIEND = 0;

		public static final int RECRUIT = 1;

		public static final int CORPORAL = 2;

		public static final int SERGEANT = 3;

		public static final int LIEUTENANT = 4;

		public static final int CAPTAIN = 5;

		public static final int GENERAL = 6;

		public static final int OWNER = 7;
	}

	public String title;

	public String founder;

	public LinkedList<String> activeMembers = new LinkedList();

	public LinkedList<String> bannedMembers = new LinkedList();

	public LinkedList<String> rankedMembers = new LinkedList();

	public LinkedList<Integer> ranks = new LinkedList();

	public int whoCanJoin = -1;

	public int whoCanTalk = -1;

	public int whoCanKick = 6;

	public int whoCanBan = 7;

	public Clan(Player paramPlayer) {
		setTitle("Friends Chat");
		setFounder(paramPlayer.getDisplay().toLowerCase());
	}

	public Clan(String paramString1, String paramString2) {
		setTitle(paramString1);
		setFounder(paramString2);
	}

	public void addMember(Player paramPlayer) {
		if (isBanned(paramPlayer.getUsername())) {
			paramPlayer.getClient()
					.queueOutgoingPacket(new SendMessage("You are currently banned from this clan chat."));
			return;
		}
		if ((this.whoCanJoin > -1) && (!isFounder(paramPlayer.getUsername()))
				&& (getRank(paramPlayer.getUsername()) < this.whoCanJoin)) {
			paramPlayer.getClient().queueOutgoingPacket(
					new SendMessage("You do not have a high enough ranking to join this chat channel."));
			return;
		}
		paramPlayer.clan = this;
		paramPlayer.lastClanChat = getFounder();
		this.activeMembers.add(paramPlayer.getUsername());
		paramPlayer.getClient().queueOutgoingPacket(new SendString("Leave chat", 18135));
		paramPlayer.getClient().queueOutgoingPacket(
				new SendString("Talking in: <col=FFFF64><shad=000000>" + getTitle() + "</shad></col>", 18139));
		paramPlayer.getClient().queueOutgoingPacket(
				new SendString("Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder()) + "</col>", 18140));
		paramPlayer.getClient().queueOutgoingPacket(new SendMessage("Attempting to join channel..."));
		paramPlayer.getClient().queueOutgoingPacket(new SendMessage(
				"Now talking in clan chat channel: <col=FFFF64><shad=000000>" + getTitle() + "</shad></col>"));
		paramPlayer.getClient()
				.queueOutgoingPacket(new SendMessage("To talk, start each line of chat with the / symbol."));
		updateMembers();
	}

	public void banMember(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			return;
		}
		if (paramString.equalsIgnoreCase(getFounder())) {
			return;
		}
		if (isRanked(paramString)) {
			return;
		}
		removeMember(paramString);
		this.bannedMembers.add(paramString);
		save();
		Player localPlayer = World.getPlayerByName(paramString);
		if ((localPlayer != null)) {
			localPlayer.getClient().queueOutgoingPacket(new SendMessage("You have been banned from the channel."));
		}
		sendMessage("Attempting to kick/ban the user '" + Misc.formatPlayerName(paramString)
				+ "' from this clan chat channel.");
	}

	public boolean canBan(String paramString) {
		if (isFounder(paramString)) {
			return true;
		}
		if (getRank(paramString) >= this.whoCanBan) {
			return true;
		}
		return false;
	}

	public boolean canKick(String paramString) {
		if (isFounder(paramString)) {
			return true;
		}
		if (getRank(paramString) >= this.whoCanKick) {
			return true;
		}
		return false;
	}

	public void delete() {
		for (String str : this.activeMembers) {
			removeMember(str);
			Player localPlayer = World.getPlayerByName(str);
			localPlayer.getClient().queueOutgoingPacket(new SendMessage("The clan you were in has been deleted."));
		}
		Engine.clanManager.delete(this);
	}

	public void demote(String paramString) {
		if (!this.rankedMembers.contains(paramString)) {
			return;
		}
		int i = this.rankedMembers.indexOf(paramString);
		this.rankedMembers.remove(i);
		this.ranks.remove(i);
		save();
	}

	public String getFounder() {
		return this.founder;
	}

	public int getRank(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.rankedMembers.contains(paramString)) {
			return this.ranks.get(this.rankedMembers.indexOf(paramString)).intValue();
		}
		if (PlayerConstants.isOwner(paramString)) {
			return 8;
		}
		if (isFounder(paramString)) {
			return 7;
		}
		return -1;
	}

	public String getRankTitle(int paramInt) {
		switch (paramInt) {
		case -1:
			return "Anyone";
		case 0:
			return "Friend";
		case 1:
			return "Recruit";
		case 2:
			return "Corporal";
		case 3:
			return "Sergeant";
		case 4:
			return "Lieutenant";
		case 5:
			return "Captain";
		case 6:
			return "General";
		case 7:
			return "Only Me";
		}
		return "";
	}

	public String getTitle() {
		return this.title;
	}

	public boolean isBanned(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			return true;
		}
		return false;
	}

	public boolean isFounder(String paramString) {
		if (getFounder().equalsIgnoreCase(paramString)) {
			return true;
		}
		return false;
	}

	public boolean isRanked(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.rankedMembers.contains(paramString)) {
			return true;
		}
		return false;
	}

	public void kickMember(String paramString) {
		if (!this.activeMembers.contains(paramString)) {
			return;
		}
		if (paramString.equalsIgnoreCase(getFounder())) {
			return;
		}
		removeMember(paramString);
		Player localPlayer = World.getPlayerByName(paramString);
		if (localPlayer != null) {
			localPlayer.getClient().queueOutgoingPacket(new SendMessage("You have been kicked from the channel."));
		}
		sendMessage("Attempting to kick/ban the user '" + Misc.formatPlayerName(paramString)
				+ "' from this clan chat channel.");
	}

	public void removeMember(Player paramPlayer) {
		for (int i = 0; i < this.activeMembers.size(); i++) {
			if (this.activeMembers.get(i).equalsIgnoreCase(paramPlayer.getUsername())) {
				paramPlayer.clan = null;
				resetInterface(paramPlayer);
				this.activeMembers.remove(i);
			}
		}
		updateMembers();
	}

	public void removeMember(String paramString) {
		for (int i = 0; i < this.activeMembers.size(); i++) {
			if (this.activeMembers.get(i).equalsIgnoreCase(paramString)) {
				Player localPlayer = World.getPlayerByName(paramString);
				if (localPlayer != null) {
					localPlayer.clan = null;
					resetInterface(localPlayer);
					this.activeMembers.remove(i);
				}
			}
		}
		updateMembers();
	}

	public void resetInterface(Player paramPlayer) {
		paramPlayer.getClient().queueOutgoingPacket(new SendString("Join Chat", 18135));
		paramPlayer.getClient().queueOutgoingPacket(new SendString("Talking in: None", 18139));
		paramPlayer.getClient().queueOutgoingPacket(new SendString("Owner: None", 18140));
		for (int i = 0; i < 100; i++) {
			paramPlayer.getClient().queueOutgoingPacket(new SendString("", 18144 + i));
		}
	}

	public void save() {
		Engine.clanManager.save(this);
		updateMembers();
	}

	public void sendChat(Player paramPlayer, String paramString) {
		if (getRank(paramPlayer.getUsername()) < this.whoCanTalk) {
			paramPlayer.getClient().queueOutgoingPacket(new SendMessage(
					"You must be atleast a " + getRankTitle(this.whoCanTalk) + " to talk in this channel."));
			return;
		}
		if (paramString.contains("@cr") || paramString.contains("<img=")) {
			paramPlayer.getClient().queueOutgoingPacket(
					new SendMessage("Your messaged contained a blocked character and could not be sent."));
			return;
		}
		for (int j = 0; j < World.getPlayers().length; j++) {
			if (World.getPlayers()[j] != null) {
				Player c = World.getPlayers()[j];
				if ((c != null) && (this.activeMembers.contains(c.getUsername()))) {
					if (paramPlayer.getRights().equals(Rights.OWNER)
							|| paramPlayer.getRights().equals(Rights.ADMINISTRATOR)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr7@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.MODERATOR)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr6@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.LEGENDARY_GOLD_MEMBER)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr5@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.EXTREME_GOLD_MEMBER)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr4@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.GOLD_MEMBER)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr3@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.IRON_MAN)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr8@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.ULTIMATE_IRON_MAN)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr9@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.HARDCORE_IRON_MAN)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr10@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else if (paramPlayer.getRights().equals(Rights.VETERAN)) {
						c.getClient()
								.queueOutgoingPacket(new SendMessage(
										"@bla@[@blu@" + getTitle() + "@bla@] " + "@cr1@ " + paramPlayer.getDisplay()
												+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					} else {
						c.getClient()
								.queueOutgoingPacket(new SendMessage("@bla@[@blu@" + getTitle() + "@bla@]<clan="
										+ getRank(paramPlayer.getUsername()) + "> " + paramPlayer.getDisplay()
										+ ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
					}
				}
			}
		}
		return;
	}

	public void sendLootshareChat(String paramString) {
		for (int j = 0; j < World.getPlayers().length; j++) {
			if (World.getPlayers()[j] != null) {
				Player c = World.getPlayers()[j];
				c.getClient().queueOutgoingPacket(new SendMessage("@bla@[@blu@" + getTitle()
						+ "@bla@] [@cr7@@dre@Lootshare]" + ":@dre@ " + Misc.capitalizeFirstLetter(paramString) + ""));
			}
		}
	}

	public void sendMessage(String paramString) {
		for (int j = 0; j < World.getPlayers().length; j++) {
			if (World.getPlayers()[j] != null) {
				Player c = World.getPlayers()[j];
				if ((c != null) && (this.activeMembers.contains(c.getUsername())))
					c.getClient().queueOutgoingPacket(new SendMessage(paramString));
			}
		}
	}

	public void setFounder(String paramString) {
		this.founder = paramString;
	}

	public void setRank(String paramString, int paramInt) {
		if (this.rankedMembers.contains(paramString)) {
			this.ranks.set(this.rankedMembers.indexOf(paramString), Integer.valueOf(paramInt));
		} else {
			this.rankedMembers.add(paramString);
			this.ranks.add(Integer.valueOf(paramInt));
		}
		save();
	}

	public void setRankCanBan(int paramInt) {
		this.whoCanBan = paramInt;
	}

	public void setRankCanJoin(int paramInt) {
		this.whoCanJoin = paramInt;
	}

	public void setRankCanKick(int paramInt) {
		this.whoCanKick = paramInt;
	}

	public void setRankCanTalk(int paramInt) {
		this.whoCanTalk = paramInt;
	}

	public void setTitle(String paramString) {
		this.title = paramString;
	}

	public void unbanMember(String paramString) {
		paramString = Misc.formatPlayerName(paramString);
		if (this.bannedMembers.contains(paramString)) {
			this.bannedMembers.remove(paramString);
			save();
		}
	}

	public void updateInterface(Player paramPlayer) {
		paramPlayer.getClient()
				.queueOutgoingPacket(new SendString("Talking in: <col=FFFF64>" + getTitle() + "</col>", 18139));
		paramPlayer.getClient()
				.queueOutgoingPacket(new SendString("Owner: <col=FFFFFF>" + (getFounder()) + "</col>", 18140));
		Collections.sort(this.activeMembers);
		for (int i = 0; i < 100; i++) {
			if (i < this.activeMembers.size()) {
				paramPlayer.getClient().queueOutgoingPacket(new SendString(
						"<clan=" + getRank(this.activeMembers.get(i)) + ">" + this.activeMembers.get(i), 18144 + i));
			} else {
				paramPlayer.getClient().queueOutgoingPacket(new SendString(" ", 18144 + i));
			}
		}
	}

	public void updateMembers() {
		for (int j = 0; j < World.getPlayers().length; j++) {
			if (World.getPlayers()[j] != null) {
				Player player = World.getPlayers()[j];
				if ((player != null) && (this.activeMembers != null)
						&& (this.activeMembers.contains(player.getUsername()))) {
					updateInterface(player);

				}
			}
		}
	}
}