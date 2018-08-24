package org.mystic.game.model.content.randomevent.impl;

import org.mystic.cache.map.Region;
import org.mystic.game.model.content.Emotes.Emote;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.randomevent.RandomEvent;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class Dwarf implements RandomEvent {

	private final int DWARF_NPC_ID = 956;

	@Override
	public boolean canFail() {
		return true;
	}

	@Override
	public boolean canStart(Player player) {
		if (player.inWilderness() || player.getDueling().isDueling() || player.getTrade().trading()) {
			return false;
		}
		if (player.getAttributes().get("randomEvent") != null) {
			if ((Boolean) player.getAttributes().get("randomEvent") == true) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void start(Player player) {
		if (!canStart(player)) {
			return;
		}
		final Npc dwarf = new Npc(player, DWARF_NPC_ID, false, false, true, getLocation(player));
		dwarf.getFollowing().setFollow(player);
		dwarf.face(player);
		dwarf.getUpdateFlags().sendForceMessage("'Ello der " + player.getDisplay() + "! *hic*");
		player.getAttributes().set("randomEvent", Boolean.valueOf(true));
		player.getAttributes().set("randomEventMob", dwarf);
		TaskQueue.queue(new Task(player, 1, true, Task.StackType.STACK, Task.BreakType.NEVER, TaskIdentifier.NONE) {

			int ticks = 50;

			@Override
			public void execute() {
				if ((Boolean) player.getAttributes().get("randomEvent") == false) {
					stop();
				} else {
					if (Misc.getManhattanDistance(player.getLocation(), dwarf.getLocation()) > 6) {
						dwarf.teleport(getLocation(player));
					}
					if (ticks == 40 || ticks == 20) {
						dwarf.getUpdateFlags()
								.sendForceMessage("Come on! *hic* Don't ignore me " + player.getDisplay() + "!");
					}
					if (ticks == 2) {
						fail(player, dwarf);
					} else if (ticks == 0) {
						stop();
					}
					ticks--;
				}
			}

			@Override
			public void onStop() {
				if (dwarf != null) {
					dwarf.remove();
				}
				player.getAttributes().set("randomEvent", Boolean.valueOf(false));
				player.getAttributes().set("randomEventMob", null);
			}
		});
	}

	@Override
	public void fail(Player player, Npc npc) {

		npc.getUpdateFlags().sendForceMessage("Fine then!");
		npc.getUpdateFlags().sendAnimation(new Animation(99));

		final int hitpoints = player.getSkill().getLevels()[Skills.HITPOINTS];
		player.hit(new Hit(Misc.random((int) (hitpoints * 0.25))));

		player.getUpdateFlags().sendGraphic(new Graphic(254, 0, true));
		player.getSkill().lock(6);
		player.setSetStunDelay(5);

		player.send(new SendMessage("The dwarf boinked you on the head before leaving."));

		player.getAttributes().set("randomEvent", Boolean.valueOf(false));
		player.getAttributes().set("randomEventMob", null);
	}

	@Override
	public void end(Player player, Npc npc) {

		player.getUpdateFlags().sendFaceToDirection(npc.getLocation());
		player.getUpdateFlags().sendAnimation(new Animation(Emote.WAVE.getAnimation()));
		player.send(new SendMessage("You send the dwarf away."));
		player.getAttributes().set("randomEvent", Boolean.valueOf(false));
		player.getAttributes().set("randomEventMob", null);
		npc.remove();

	}

	@Override
	public void reward(Player player) {

	}

	/**
	 * Gets a good location from where the player is standing
	 * 
	 * @param player
	 * @return
	 */
	public Location getLocation(Player player) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		final int z = player.getLocation().getZ();
		if (Region.getRegion(player.getLocation().getX(), player.getLocation().getY()) != null) {
			if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedWest(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				x--;
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedEast(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				x++;
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedNorth(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				y++;
			} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedSouth(
					player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
				y--;
			}
		}
		return new Location(x, y, z);
	}

}