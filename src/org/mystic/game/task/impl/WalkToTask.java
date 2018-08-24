package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.impl.GroundItem;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public abstract class WalkToTask extends Task {

	private final int minX, maxX, minY, maxY;

	private final Location location;

	public WalkToTask(Player player, GroundItem ground) {
		this(player, ground.getLocation().getX(), ground.getLocation().getY(), 1, 1);
	}

	public WalkToTask(Player player, int x, int y, int xLength, int yLength) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.location = player.getLocation();
		this.minX = x - 1;
		this.maxX = minX + xLength + 1;
		this.minY = y - 1;
		this.maxY = minY + yLength + 1;
	}

	public WalkToTask(Player player, Player other) {
		this(player, other.getLocation().getX(), other.getLocation().getY(), 1, 1);
	}

	@Override
	public void execute() {
		int pX = location.getX();
		int pY = location.getY();
		if (pX >= minX && pX <= maxX && pY >= minY && pY <= maxY) {
			onDestination();
			stop();
		}
	}

	public abstract void onDestination();

	@Override
	public void onStop() {

	}

}