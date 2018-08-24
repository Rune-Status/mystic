package org.mystic.game.model.networking.packet.command.impl;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.Command;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.model.player.io.PlayerSaveUtil;
import org.mystic.utility.Misc;

public class PunishmentCommand implements Command {

	public static void ban(Player player, int length) {
		player.setBanned(true);
		player.setBanDay(Misc.getDayOfYear());
		player.setBanYear(Misc.getYear());
		player.setBanLength(length);
		player.logout(true);
	}

	public static void mute(Player player, int length) {
		player.setMuted(true);
		player.setRemainingMute(length);
		player.setMuteDay(Misc.getDayOfYear());
		player.setMuteYear(Misc.getYear());
		player.setMuteLength(length);
	}

	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		switch (args[0]) {
		case "mute":
			if (args.length == 3) {
				final int length = Integer.parseInt(args[2]);
				if (length > 0) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						if (punish.getRights().greater(player.getRights())) {
							player.send(new SendMessage("You may not mute this player."));
							return;
						} else {
							punish.setMuted(true);
							punish.setRemainingMute(length);
							punish.setMuteDay(Misc.getDayOfYear());
							punish.setMuteYear(Misc.getYear());
							punish.setMuteLength(length);
						}
					} else {
						if (!PlayerSaveUtil.muteOfflinePlayer(name, length)) {
							player.getClient()
									.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
							return;
						}
					}
					if (player.getRights().equals(Rights.MODERATOR)
							|| player.getRights().equals(Rights.ADMINISTRATOR)) {
						PlayerLogs.log(player.getUsername(), "Muted " + "name" + " for " + length + " days.");
					}
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You have successfully muted " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive mute length."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: mute name days"));
			}
			break;
		case "yellmute":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				final Player punish = World.getPlayerByName(name);
				if (punish != null) {
					if (punish.getRights().greater(player.getRights())) {
						player.send(new SendMessage("You may not yell-mute this player."));
						return;
					} else {
						punish.setYellMuted(true);
					}
				} else {
					if (!PlayerSaveUtil.yellMuteOfflinePlayer(name)) {
						player.getClient()
								.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
						return;
					}
				}
				if (player.getRights().equals(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) {
					PlayerLogs.log(player.getUsername(), "Yell-Muted the player " + name + ".");
				}
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You have successfully yell muted " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: yellmute name"));
			}
			break;
		case "unyellmute":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				final Player punish = World.getPlayerByName(name);
				if (punish != null) {
					punish.setYellMuted(false);
				} else {
					if (!PlayerSaveUtil.unYellMuteOfflinePlayer(name)) {
						player.getClient()
								.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
						return;
					}
				}
				if (player.getRights().equals(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) {
					PlayerLogs.log(player.getUsername(), "Removed a yell-mute from the player (" + name + ").");
				}
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You have successfully yell muted " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: yellmute name"));
			}
			break;
		case "unmute":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				final Player p2 = World.getPlayerByName(name);
				if (p2 != null) {
					p2.setMuted(false);
				} else {
					if (!PlayerSaveUtil.unmuteOfflinePlayer(name)) {
						player.getClient()
								.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
						return;
					}
				}
				if (player.getRights().equals(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) {
					PlayerLogs.log(player.getUsername(), "Removed a mute from the player (" + name + ").");
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unmuted " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unmute name"));
			}
			break;

		case "ban":
			if (args.length == 3) {
				final int length = Integer.parseInt(args[2]);
				if (length > 0) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						if (punish.getRights().greater(player.getRights())) {
							player.send(new SendMessage("You may not ban this player."));
							return;
						} else {
							punish.setBanned(true);
							punish.setBanDay(Misc.getDayOfYear());
							punish.setBanYear(Misc.getYear());
							punish.setBanLength(length);
							punish.logout(true);
						}
					} else {
						if (!PlayerSaveUtil.banOfflinePlayer(name, length)) {
							player.getClient()
									.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
							return;
						}
					}
					if (player.getRights().equals(Rights.MODERATOR)
							|| player.getRights().equals(Rights.ADMINISTRATOR)) {
						PlayerLogs.log(player.getUsername(),
								"Banned the player" + "name" + " for " + length + " days.");
					}
					player.getClient().queueOutgoingPacket(
							new SendMessage("You have successfully banned " + name + " for " + length + " days."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive ban length."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: ban name days"));
			}
			break;

		case "unban":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				if (!PlayerSaveUtil.unbanOfflinePlayer(name)) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("The player (" + name + ") does not exist."));
					return;
				}
				if (player.getRights().equals(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) {
					PlayerLogs.log(player.getUsername(), "Removed a ban from the player " + name + ").");
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unbanned " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unban name"));
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
		return Rights.MODERATOR;
	}

}