package org.mystic.game.model.content;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendFriendUpdate;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendPMServer;
import org.mystic.game.model.networking.outgoing.SendPrivateMessage;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.utility.Misc;
import org.mystic.utility.NameUtil;

public class PrivateMessaging {

	private final Player player;

	private List<String> friends = new LinkedList<String>();

	private List<String> ignores = new LinkedList<String>();

	private int messagesReceived = 0;

	public PrivateMessaging(Player player) {
		this.player = player;
	}

	public void addFriend(long id) {
		final String name = NameUtil.longToName(id).toLowerCase().replaceAll("_", " ");
		if (name == player.getUsername()) {
			player.send(new SendMessage("You cant add yourself!"));
			return;
		}
		friends.add(name);
		player.getAchievements().incr(player, "Have 25 friends");
		player.getClient().queueOutgoingPacket(new SendFriendUpdate(id, World.getPlayerByName(name) == null ? 0 : 1));
	}

	public void addFriend(String name) {
		name = name.toLowerCase();
		long id = NameUtil.nameToLong(name);
		if (name == player.getUsername()) {
			player.send(new SendMessage("You cant add yourself!"));
			return;
		}
		friends.add(name);
		player.getAchievements().set(player, "Have 25 friends", friends.size(), false);
		player.getClient().queueOutgoingPacket(new SendFriendUpdate(id, World.getPlayerByName(name) == null ? 0 : 1));
	}

	public void addIgnore(long id) {
		final Player add = World.getPlayerByName(id);
		if (add != null) {
			if (add.getRights().getValue() > player.getRights().getValue()) {
				return;
			}
			ignores.add(NameUtil.longToName(id).replaceAll("_", " "));
		}
	}

	public void addIgnore(String name) {
		final Player add = World.getPlayerByName(name);
		if (add != null) {
			if (add.getRights().getValue() > player.getRights().getValue()) {
				return;
			}
			ignores.add(name);
		}
	}

	public void connect() {
		player.getClient().queueOutgoingPacket(new SendPMServer(2));
		for (Iterator<String> i = friends.iterator(); i.hasNext();) {
			String name = i.next();
			player.getClient().queueOutgoingPacket(
					new SendFriendUpdate(NameUtil.nameToLong(name), World.getPlayerByName(name) == null ? 0 : 1));
		}
		player.getAchievements().set(player, "Have 25 friends", friends.size(), false);
	}

	public List<String> getFriends() {
		return friends;
	}

	public List<String> getIgnores() {
		return ignores;
	}

	public int getNextMessageId() {
		return messagesReceived++;
	}

	public boolean ignored(String n) {
		return ignores.contains(n.toLowerCase());
	}

	public void removeFriend(long id) {
		if (friends.contains(NameUtil.longToName(id))) {
			friends.remove(NameUtil.longToName(id).replaceAll("_", " "));
		} else {
			player.send(new SendMessage("This player is not on your friends list!"));
		}
	}

	public void removeIgnore(long id) {
		if (ignores.contains(NameUtil.longToName(id))) {
			ignores.remove(NameUtil.longToName(id).replaceAll("_", " "));
		} else {
			player.send(new SendMessage("This player is not on your ignore list!"));
		}
	}

	public void sendPrivateMessage(long id, int size, byte[] text) {
		final String name = NameUtil.longToName(id).replaceAll("_", " ");
		final Player sentTo = World.getPlayerByName(name);
		if (sentTo != null) {
			if (sentTo.getPrivateMessaging().ignored(player.getUsername())) {
				player.send(new SendMessage("This player is ignoring you."));
				return;
			}
			if (player.isMuted()) {
				player.getClient().queueOutgoingPacket(new SendMessage(
						"You are muted, you will be unmuted in " + player.getRemainingMute() + " days."));
				return;
			}
			sentTo.getClient().queueOutgoingPacket(new SendPrivateMessage(NameUtil.nameToLong(player.getUsername()),
					player.getRights().getProtocolValue(), text, sentTo.getPrivateMessaging().getNextMessageId()));
			PlayerLogs.log(player.getUsername(),
					"Sent Private-Message to User: " + name + " and said '" + Misc.textUnpack(text, size) + "'");
			PlayerLogs.log(sentTo.getUsername(), "Recieved Private-Message from User: " + player.getUsername()
					+ " which said '" + Misc.textUnpack(text, size) + "'");
		} else {
			player.getClient().queueOutgoingPacket(new SendMessage("Your message could not be delivered."));
		}
	}

	public void updateOnlineStatus(Player connectedPlayer, boolean connected) {
		final String name = connectedPlayer.getUsername().toLowerCase();
		if (friends.contains(name)) {
			player.getClient().queueOutgoingPacket(new SendFriendUpdate(NameUtil.nameToLong(name), connected ? 1 : 0));
		}
	}
}