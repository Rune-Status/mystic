package org.mystic.game.task.impl;

import java.util.HashMap;
import java.util.Map;

import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.Area;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

/**
 * Managment of the Wilderness Teleportation Obelisk
 */
public class ObeliskTeleportation extends Task {

	public enum Obelisk {

		A(3155, 3157, 3619, 3621, 14829, new Location(3154, 3618)),

		B(3226, 3228, 3666, 3668, 14830, new Location(3225, 3665)),

		C(3034, 3036, 3731, 3733, 14827, new Location(3033, 3730)),

		D(3105, 3107, 3793, 3795, 14828, new Location(3104, 3792)),

		E(2979, 2981, 3865, 3867, 14826, new Location(2978, 3864)),

		F(3306, 3308, 3915, 3917, 14831, new Location(3305, 3914));

		private int topLeftX, bottomRightX, bottomRightY, topLeftY;
		private Location Location;
		private boolean active;
		private Area gfxArea;
		private Location[] pillars;
		private int pillarId;

		Obelisk(int topLeftX, int bottomRightX, int bottomRightY, int topLeftY, int pillarId, Location Location) {
			this.topLeftX = topLeftX;
			this.bottomRightX = bottomRightX;
			this.bottomRightY = bottomRightY;
			this.topLeftY = topLeftY;
			this.setPillarId(pillarId);
			this.Location = Location;
			this.active = false;
			this.gfxArea = Area.areaFromCorner(new org.mystic.game.model.entity.Location(Location.getX() + 1,
					Location.getY() + 1, Location.getZ()), 2, 2);
			this.pillars = getObeliskLocations(this);
		}

		public int getBottomRightX() {
			return bottomRightX;
		}

		public int getBottomRightY() {
			return bottomRightY;
		}

		public Area getGfxArea() {
			return gfxArea;
		}

		public int getPillarId() {
			return pillarId;
		}

		public int getTopLeftX() {
			return topLeftX;
		}

		public int getTopLeftY() {
			return topLeftY;
		}

		public void setPillarId(int pillarId) {
			this.pillarId = pillarId;
		}
	}

	private static final int TICKS = 7;

	private static final int ACTIVE = 14825;

	/**
	 * The state of each obelisk, if they are either active or inactive.
	 */
	private static Map<Integer, Boolean> state = new HashMap<>();

	/**
	 * Stores the obelisk object ids with the default state, false, in a map.
	 */
	static {
		for (Obelisk location : Obelisk.values()) {
			state.put(location.getPillarId(), false);
		}
	}

	public static boolean clickObelisk(int objectId) {
		for (Obelisk obelisk : Obelisk.values()) {
			if (obelisk.getPillarId() == objectId) {
				if (obelisk.active) {
					return false;
				}
				boolean active = state.get(objectId);
				if (active) {
					return false;
				}
				state.put(objectId, true);
				Task task = new ObeliskTeleportation(obelisk);
				TaskQueue.queue(task);
				return true;
			}
		}
		return false;
	}

	private static Location[] getObeliskLocations(Obelisk obelisk) {
		Location location = obelisk.Location;
		int i = 0;
		Location[] locations = new Location[4];
		for (int xMod = 0; xMod <= 4; xMod += 4) {
			for (int yMod = 0; yMod <= 4; yMod += 4) {
				locations[i++] = new Location(location.getX() + xMod, location.getY() + yMod, location.getZ());
			}
		}
		return locations;
	}

	/**
	 * Checks if the player is within the Obelisk
	 */
	private static boolean isOnObelisk(Player player, Obelisk ob) {
		return (player.getLocation().getX() >= ob.getTopLeftX() && player.getLocation().getX() <= ob.getBottomRightX()
				&& player.getLocation().getY() >= ob.getBottomRightY()
				&& player.getLocation().getY() <= ob.getTopLeftY());
	}

	public static void main(String[] args) {
		getObeliskLocations(Obelisk.A);
	}

	private Obelisk obelisk;

	public ObeliskTeleportation(Obelisk obelisk) {
		super(TICKS, true);
		this.obelisk = obelisk;
	}

	private void activateObelisk(Obelisk obelisk) {
		for (Location pillar : obelisk.pillars) {
			TaskQueue.queue(new RegenerateObeliskTask(Region.getObject(pillar.getX(), pillar.getY(), pillar.getZ()),
					new GameObject(ACTIVE, pillar.getX(), pillar.getY(), pillar.getZ(), 10, 0), obelisk, 1, 8));
		}
	}

	@Override
	public void execute() {
		if (!obelisk.active) {
			obelisk.active = true;
			activateObelisk(obelisk);
			return;
		}
		stop();
		obelisk.active = false;
		Obelisk to;
		state.put(obelisk.getPillarId(), false);
		while (true) {
			int random = Misc.randomNumber(Obelisk.values().length);
			if (Obelisk.values()[random] == obelisk) {
				continue;
			}
			to = Obelisk.values()[random];
			break;
		}
		for (Player player : World.getPlayers()) {
			if (player == null) {
				continue;
			}
			if (isOnObelisk(player, obelisk)) {
				teleport(player, to);
			}
		}
		Location[] teleArea = obelisk.getGfxArea().calculateAllLocations();
		for (Location Location : teleArea) {
			World.sendStillGraphic(342, 0, Location);
		}
	}

	@Override
	public void onStop() {
		obelisk.active = false;
	}

	private void teleport(Player player, Obelisk to) {
		int deltaX = player.getLocation().getX() - obelisk.Location.getX();
		int deltaY = player.getLocation().getY() - obelisk.Location.getY();
		player.getMagic().doWildernessTeleport(to.Location.getX() + deltaX, to.Location.getY() + deltaY,
				to.Location.getZ(), TeleportTypes.OBELISK);
	}

}