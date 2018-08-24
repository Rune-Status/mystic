package org.mystic.game.task.impl;

import org.mystic.game.World;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.pathfinding.SimplePathWalker;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.utility.Misc;

public class MobWalkTask extends Task {

	private final Npc mob;

	private final Location l;

	private byte wait = 0;

	private final boolean shouldWait;

	public MobWalkTask(Npc mob, Location l, boolean shouldWait) {
		super(mob, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.mob = mob;
		mob.setForceWalking(true);
		this.l = l;
		this.shouldWait = shouldWait;
	}

	@Override
	public void execute() {
		if (mob.isDead()) {
			stop();
			return;
		}

		if (Misc.getManhattanDistance(l, mob.getLocation()) <= 0) {
			stop();
		} else {
			SimplePathWalker.walkToNextTile(mob, l);

			if (mob.getMovementHandler().getPrimaryDirection() == -1) {
				if (shouldWait) {
					if (wait != 0) {
						wait = ((byte) (wait - 1));
						return;
					}

					for (Player p : World.getPlayers()) {
						if ((p != null) && (mob.getLocation().isViewableFrom(p.getLocation()))) {
							wait = 15;
							return;
						}
					}

					mob.teleport(l);
				}

				stop();
			} else {
				mob.getCombat().reset();
			}
		}
	}

	@Override
	public void onStop() {
		mob.setForceWalking(false);
	}
}
