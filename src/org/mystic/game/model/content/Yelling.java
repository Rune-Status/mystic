package org.mystic.game.model.content;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class Yelling {

	public static final String YELL_COOLDOWN_KEY = "yellcooldown";

	public static final String PREFIX = "@dre@(Yell) ";

	public static String send;

	public static void yell(Player player, String message) {
		if (player.isMuted()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are muted and cannot yell."));
			return;
		}
		if (player.isYellMuted()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are yell-muted are not cannot yell."));
			return;
		}
		if (message.contains("<") || message.contains(">") || message.contains("^") || message.contains("@cr")
				|| message.contains("<=img")) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot use text arguments when yelling."));
			return;
		}
		if (player.getAttributes().get("yellcooldown") == null) {
			player.getAttributes().set("yellcooldown", Long.valueOf(System.currentTimeMillis()));
		} else if (System.currentTimeMillis()
				- ((Long) player.getAttributes().get("yellcooldown")).longValue() < 3000L) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You must wait a few seconds before yelling again."));
			return;
		}
		Rights rights = player.getRights();
		if (player.getRights().equals(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)
				|| player.getRights().equals(Rights.OWNER)) {
			if (rights.equal(Rights.OWNER)) {
				send = "@bla@[@cr7@@blu@Owner@bla@] - " + player.getDisplay() + ": "
						+ Misc.capitalizeFirstLetter(message);
			} else if (rights.equal(Rights.ADMINISTRATOR)) {
				send = "@bla@[@cr7@@blu@Administrator@bla@] - " + player.getDisplay() + ": "
						+ Misc.capitalizeFirstLetter(message);
			} else if (rights.equal(Rights.MODERATOR)) {
				send = "@bla@[@cr6@@blu@Moderator@bla@] - " + player.getDisplay() + ": "
						+ Misc.capitalizeFirstLetter(message);
			}
			if (send != null) {
				World.sendGlobalMessage(PREFIX + send, true);
			}
			return;
		} else {
			if (!player.isGoldMember()) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You must be a Gold Member to use the yell channel."));
				player.getClient().queueOutgoingPacket(
						new SendMessage("Visit the store @ www.mystic-ps.com/store/ to view available packages."));
				return;
			}
			if (rights.equal(Rights.LEGENDARY_GOLD_MEMBER)) {
				send = "@bla@[@cr5@@dre@Legendary Member@bla@] - " + player.getDisplay() + ": "
						+ Misc.capitalizeFirstLetter(message);
			} else if (rights.equal(Rights.EXTREME_GOLD_MEMBER)) {
				send = "@bla@[@cr4@@or2@Extreme Member@bla@] - " + player.getDisplay() + ": "
						+ Misc.capitalizeFirstLetter(message);
			} else if (rights.equal(Rights.GOLD_MEMBER)) {
				send = "@bla@[@cr3@@or2@Gold Member@bla@] - " + player.getDisplay() + ": " + message;
			}
			player.getAttributes().set("yellcooldown", Long.valueOf(System.currentTimeMillis()));
			if (send != null) {
				World.sendGlobalMessage(PREFIX + send, true);
			}
			return;
		}
	}
}
