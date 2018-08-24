package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.following.Following.FollowType;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public abstract class FollowToEntityTask extends Task {

	private final Location location;

	private final Entity player;

	private final int minX, maxX, minY, maxY;

	public FollowToEntityTask(Entity player, Entity entity) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.player.getFollowing().setFollow(entity, FollowType.FOLLOW_TO);
		this.location = player.getLocation();
		this.minX = entity.getLocation().getX() - 1;
		this.maxX = minX + entity.getSize() + 1;
		this.minY = entity.getLocation().getY() - 1;
		this.maxY = minY + entity.getSize() + 1;

	}

	@Override
	public void execute() {
		int pX = location.getX();
		int pY = location.getY();
		if (pX >= minX && pX <= maxX && pY >= minY && pY <= maxY) {
			player.getFollowing().reset();
			onDestination();
			stop();
		}
	}

	public abstract void onDestination();

	@Override
	public void onStop() {

	}

}