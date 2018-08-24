package org.mystic.game.model.entity.pathfinding;

import org.mystic.game.GameConstants;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.Walking;

public class SimplePathWalker {

	public static boolean walkable(Entity entity, int i) {
		return entity.getMovementHandler().canMoveTo(i);
	}

	public static void walkToNextTile(Npc mob, Location waypoint) {
		if (mob.getLocation().equals(waypoint)) {
			return;
		}
		int direction = -1;
		final int x = mob.getLocation().getX();
		final int y = mob.getLocation().getY();
		final int xDifference = waypoint.getX() - mob.getLocation().getX();
		final int yDifference = waypoint.getY() - mob.getLocation().getY();
		int toX = 0;
		int toY = 0;
		if (xDifference > 0) {
			toX = 1;
		} else if (xDifference < 0) {
			toX = -1;
		}
		if (yDifference > 0) {
			toY = 1;
		} else if (yDifference < 0) {
			toY = -1;
		}
		int toDir = GameConstants.getDirection(x, y, x + toX, y + toY);
		if (walkable(mob, toDir)) {
			direction = toDir;
		} else {
			if (toDir == 0) {
				if (walkable(mob, 3)) {
					direction = 3;
				} else if (walkable(mob, 1)) {
					direction = 1;
				}
			} else if (toDir == 2) {
				if (walkable(mob, 1)) {
					direction = 1;
				} else if (walkable(mob, 4)) {
					direction = 4;
				}
			} else if (toDir == 5) {
				if (walkable(mob, 3)) {
					direction = 3;
				} else if (walkable(mob, 6)) {
					direction = 6;
				}
			} else if (toDir == 7) {
				if (walkable(mob, 4)) {
					direction = 4;
				} else if (walkable(mob, 6)) {
					direction = 6;
				}
			}
		}
		if (direction == -1) {
			return;
		}
		Walking.walk(mob, direction);
	}
}
