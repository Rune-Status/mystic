package org.mystic.game.model.entity.movement.impl;

import org.mystic.game.model.entity.movement.MovementHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.Walking;

public class MobMovementHandler extends MovementHandler {

	private final Npc mob;

	public MobMovementHandler(Npc mob) {
		super(mob);
		this.mob = mob;
	}

	@Override
	public boolean canMoveTo(int direction) {
		return Walking.canMoveTo(mob, direction);
	}

	@Override
	public boolean canMoveTo(int x, int y, int size, int direction) {
		return Walking.canMoveTo(mob, x, y, direction, size);
	}

	@Override
	public boolean moving() {
		return mob.getFollowing().isFollowing();
	}

	@Override
	public void process() {
	}
}
