package org.mystic.game.model.definition;

import org.mystic.game.model.entity.Location;

public class NpcSpawnDefinition {

	private short id;
	private Location location;
	private boolean walk;
	private byte face = -1;

	public int getFace() {
		return face;
	}

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isWalk() {
		return walk;
	}
}
