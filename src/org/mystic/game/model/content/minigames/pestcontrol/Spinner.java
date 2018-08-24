package org.mystic.game.model.content.minigames.pestcontrol;

import org.mystic.game.model.entity.Location;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.FollowToEntityTask;
import org.mystic.utility.Misc;

public class Spinner extends Pest {

	private final Portal portal;
	private final Task heal = null;

	public Spinner(Location l, PestControlGame game, Portal portal) {
		super(game, PestControlConstants.SPINNERS[Misc.randomNumber(PestControlConstants.SPINNERS.length)], l);
		setRetaliate(false);
		this.portal = portal;
	}

	public void heal() {
		if ((heal == null) || (heal.stopped())) {
			TaskQueue.queue(new FollowToEntityTask(this, portal) {
				@Override
				public void onDestination() {
					getUpdateFlags().sendAnimation(3911, 0);
					portal.heal(10 + Misc.randomNumber(15));
					stop();
				}
			});
		}
	}

	@Override
	public void tick() {
		if (portal.isDead()) {
			return;
		}

		if ((portal.isDamaged()) && (Misc.randomNumber(3) == 0)) {
			heal();
		}
	}
}
