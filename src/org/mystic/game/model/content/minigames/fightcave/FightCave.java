package org.mystic.game.model.content.minigames.fightcave;

import java.util.ArrayList;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.VirtualNpcRegion;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.player.io.PlayerLogs;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public final class FightCave {

	public static final CavesController CONTROLLER = new CavesController();

	public static final Location LEAVE = new Location(2440, 5170, 0);

	public static final String FIGHT_CAVE_NPCS_KEY = "fightcavesnpcs";

	public static final Location[] SPAWN_LOCATIONS = { new Location(2411, 5109), new Location(2413, 5105),
			new Location(2385, 5106), new Location(2380, 5102), new Location(2380, 5073), new Location(2387, 5071),
			new Location(2420, 5082), new Location(2416, 5107), new Location(2412, 5111), new Location(2382, 5108),
			new Location(2378, 5103) };

	public static void checkForFightCave(Player player, Npc mob) {
		if (player.getController().equals(CONTROLLER)) {
			if (mob.getId() == Wave.NPCS_DETAILS.TZTOK_JAD) {
				finish(player, true);
			} else {
				player.getJadDetails().removeNpc(mob);
				if (player.getJadDetails().getKillAmount() == 0) {
					player.getJadDetails().increaseStage();
					startNextWave(player);
				}
			}
		}
	}

	public static void finish(Player player, boolean reward) {
		if (reward) {
			PlayerLogs.log(player.getUsername(), "Completed the Fight caves and recieved a fire cape.");
			player.getUpdateFlags().sendAnimation(new Animation(862));
			player.getInventory().addOrCreateGroundItem(6570, 1, true);
			player.getInventory().addOrCreateGroundItem(6529, 5000, true);
			player.getClient()
					.queueOutgoingPacket(new SendMessage("@dre@Congratulations on your completion of the caves."));
		} else {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("@dre@You did not make it far enough to recieve a reward."));
		}
		onLeaveGame(player);
		player.teleport(LEAVE);
	}

	public static void init(Player player) {
		player.getSkulling().unskull(player);
		player.getSkill().restore();
		player.getRunEnergy().setEnergy(100);
		player.getSpecialAttack().setSpecialAmount(100);
		player.send(new SendRemoveInterfaces());
		player.setController(CONTROLLER);
		if (player.isGoldMember()) {
			player.getJadDetails().setStage(Wave.WAVE_50.ordinal());
			player.send(new SendMessage("@dre@You have been placed on Wave: 50 since you are a gold member."));
		} else {
			player.getJadDetails().setStage(Wave.WAVE_38.ordinal());
			player.send(new SendMessage("@dre@You have been placed on Wave: 38. Gold Members will start on Wave 50."));
		}
		if (player.getJadDetails().getZ() == 0) {
			player.getJadDetails().setZ(player);
		}
		player.teleport(new Location(2413, 5117, player.getJadDetails().getZ()));
		startNextWave(player);
	}

	public static void loadGame(Player player) {
		player.setController(CONTROLLER);
		if (player.getJadDetails().getStage() != 0) {
			startNextWave(player);
		}
	}

	public static final void onLeaveGame(Player player) {
		for (Npc i : player.getJadDetails().getMobs()) {
			if (i != null) {
				i.remove();
			}
		}
		player.getJadDetails().getMobs().clear();
		player.getJadDetails().reset();
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	public static void startNextWave(Player player) {
		if (player.getJadDetails().getZ() == 0) {
			player.getJadDetails().setZ(player);
			player.changeZ(player.getJadDetails().getZ());
		}
		TaskQueue.queue(new Task(player, 4) {
			@Override
			public void execute() {
				final ArrayList<Location> randomizedSpawns = new ArrayList<Location>();
				for (Location i : SPAWN_LOCATIONS) {
					randomizedSpawns.add(i);
				}
				VirtualNpcRegion r = new VirtualNpcRegion(2411, 5109, 400);
				for (int i : Wave.values()[player.getJadDetails().getStage()].getNpcs()) {
					int c = Misc.randomNumber(randomizedSpawns.size());
					Location loc = new Location(randomizedSpawns.get(c));
					randomizedSpawns.remove(c);
					loc.setZ(player.getJadDetails().getZ());
					Npc mob = new Npc(i, false, loc, player, false, false, r);
					mob.getFollowing().setIgnoreDistance(true);
					mob.getCombat().setAttack(player);
					player.getJadDetails().addNpc(mob);
				}
				stop();
			}

			@Override
			public void onStop() {
				player.send(new SendMessage("@dre@Wave: " + player.getJadDetails().getStage() + "."));
			}
		});
	}
}