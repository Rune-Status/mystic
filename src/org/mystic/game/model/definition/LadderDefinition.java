package org.mystic.game.model.definition;

import org.mystic.game.model.entity.Location;

public class LadderDefinition {

	private final Location location;

	private final Location dest;

	private final ClimbType type;

	public enum ClimbType {

		UP, DOWN;
	}

	public Location getPOS() {
		return location;
	}

	public Location getDEST() {
		return dest;
	}

	public ClimbType getClimbType() {
		return type;
	}

	public LadderDefinition(final Location location, final Location dest, final ClimbType type) {
		this.location = location;
		this.dest = dest;
		this.type = type;
	}
}
