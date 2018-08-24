package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;

public abstract class DesertStrykeworm extends Npc {

	public static void spawn() {
		new DesertStrykeworm(2029, 5234) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2043, 5243);
			}
		};
		new DesertStrykeworm(2042, 5189) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2041, 5215);
			}
		};
		new DesertStrykeworm(2006, 5203) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2017, 5214);
			}
		};
		new DesertStrykeworm(1991, 5238) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2015, 5235);
			}
		};
	}

	private boolean teleported = false;

	public DesertStrykeworm(int x, int y) {
		super(9465, true, new Location(x, y));
	}

	@Override
	public void doAliveMobProcessing() {
		teleport();
	}

	public abstract Location getTeleportToLocation();

	@Override
	public boolean isWalkToHome() {
		return false;
	}

	@Override
	public void onDeath() {
		teleported = false;
	}

	public void teleport() {
		if (!teleported) {
			if (getLevels()[3] < getMaxLevels()[3] / 2) {
				teleported = true;
				TaskQueue.queue(new Task(this, 1) {

					private byte timer = 0;

					@Override
					public void execute() {
						timer = ((byte) (timer + 1));
						getCombat().setAttackTimer(5);
						if (timer == 8) {
							getUpdateFlags().sendAnimation(new Animation(12796, 0));
						} else if (timer == 10) {
							stop();
						}
					}

					@Override
					public void onStop() {
						teleport(getTeleportToLocation());
					}
				});
			}
		}
	}

	@Override
	public boolean withinMobWalkDistance(Entity e) {
		return true;
	}

}