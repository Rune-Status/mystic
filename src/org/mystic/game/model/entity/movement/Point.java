package org.mystic.game.model.entity.movement;

import org.mystic.game.model.entity.Location;

public class Point extends Location {
	private int direction;

	public Point(int x, int y, int direction) {
		super(x, y);
		setDirection(direction);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
