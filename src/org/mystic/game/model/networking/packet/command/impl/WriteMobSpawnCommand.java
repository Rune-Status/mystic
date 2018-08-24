package org.mystic.game.model.networking.packet.command.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.mystic.game.World;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.packet.command.Command;

public class WriteMobSpawnCommand implements Command {

	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		try {
			int npcId = Integer.parseInt(args[1]);
			World.register(new Npc(npcId, false, new Location(player.getLocation())));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./data/spawn command dump.txt"), true));
			bw.newLine();
			bw.write("\t<NpcSpawnDefinition>", 0, "\t<NpcSpawnDefinition>".length());
			bw.newLine();
			bw.write("\t\t<id>" + npcId + "</id>", 0, ("\t\t<id>" + npcId + "</id>").length());
			bw.newLine();
			bw.write("\t\t<location>", 0, "\t\t<location>".length());
			bw.newLine();
			bw.write("\t\t\t<x>" + player.getLocation().getX() + "</x>", 0,
					("\t\t\t<x>" + player.getLocation().getX() + "</x>").length());
			bw.newLine();
			bw.write("\t\t\t<y>" + player.getLocation().getY() + "</y>", 0,
					("\t\t\t<y>" + player.getLocation().getY() + "</y>").length());
			bw.newLine();
			bw.write("\t\t\t<z>" + player.getLocation().getZ() + "</z>", 0,
					("\t\t\t<z>" + player.getLocation().getZ() + "</z>").length());
			bw.newLine();
			bw.write("\t\t</location>", 0, "\t\t</location>".length());
			bw.newLine();
			bw.write("\t\t<walk>true</walk>", 0, "\t\t<walk>true</walk>".length());
			bw.newLine();
			bw.write("\t</NpcSpawnDefinition>", 0, "\t</NpcSpawnDefinition>".length());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return (PlayerConstants.isOwner(player));
	}

	@Override
	public Rights rightsRequired() {
		return Rights.OWNER;
	}
}
