package org.mystic.game.task;

import org.mystic.game.model.entity.Entity;

public abstract class RunOnceTask extends Task {

	public RunOnceTask(Entity entity, int delay) {
		super(entity, delay);
	}

	@Override
	public void execute() {
		stop();
	}

	@Override
	public abstract void onStop();

}