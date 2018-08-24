package org.mystic.game.model.networking.packet.command;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.packet.command.impl.EmptyInventoryCommand;
import org.mystic.game.model.networking.packet.command.impl.HybridSetupCommand;
import org.mystic.game.model.networking.packet.command.impl.PlayersOnlineCommand;
import org.mystic.game.model.networking.packet.command.impl.PunishmentCommand;
import org.mystic.game.model.networking.packet.command.impl.ReloadCommand;
import org.mystic.game.model.networking.packet.command.impl.SpawnObjectCommand;
import org.mystic.game.model.networking.packet.command.impl.SwitchMagicInterfaceCommand;
import org.mystic.game.model.networking.packet.command.impl.TeleportCommand;

public class CommandManager {

	private static final Map<String, Command> commands = new HashMap<String, Command>();

	static {

		put("hybrid", new HybridSetupCommand());
		put("switch", new SwitchMagicInterfaceCommand());
		put("players", new PlayersOnlineCommand());
		put("rdrops", new ReloadCommand());
		put("object", new SpawnObjectCommand());
		put("empty", new EmptyInventoryCommand());

		TeleportCommand teleport = new TeleportCommand();
		put("goto", teleport);
		put("summon", teleport);

		PunishmentCommand punish = new PunishmentCommand();
		put("mute", punish);
		put("unmute", punish);
		put("ban", punish);
		put("unban", punish);
		put("yellmute", punish);
		put("unyellmute", punish);
		put("blackmark", punish);
		put("bm", punish);

	}

	public static boolean handleCommand(Player player, String command) {
		String name = "";
		if (command.indexOf(' ') > -1) {
			name = command.substring(0, command.indexOf(' '));
		} else {
			name = command;
		}
		name = name.toLowerCase();
		Command comm = commands.get(name);
		if (comm != null) {
			if (!comm.meetsRequirements(player) && !PlayerConstants.isOwner(player)) {
				return false;
			}
			if (player.getRights().greater(comm.rightsRequired()) || player.getRights().equal(comm.rightsRequired())) {
				try {
					comm.handleCommand(player, command);
				} catch (Exception e) {
					player.getClient().queueOutgoingPacket(new SendMessage("Invalid command format."));
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	private static void put(String name, Command command) {
		commands.put(name, command);
	}

	public static void reloadCommands() {
		new CommandManager();
	}

}