package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class MorrigansJavelinSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public void handleAttack(Player player) {
		player.getCombat().getRanged().setStart(new Graphic(1836, 0, true));

		Entity e = player.getCombat().getAttacking();

		if ((e != null) && (!e.isNpc())) {
			final Player p = org.mystic.game.World.getPlayers()[e.getIndex()];

			if (p != null)
				TaskQueue.queue(new Task(p, 2) {
					private byte c = 0;

					@Override
					public void execute() {
						if (p.isDead() || !p.getController().allowPvPCombat()) {
							stop();
							return;
						}

						p.hit(new Hit(Misc.randomNumber(5) + 1));

						if ((this.c = (byte) (c + 1)) == 9)
							stop();
					}

					@Override
					public void onStop() {
					}
				});
		}
	}
}
