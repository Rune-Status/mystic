package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.HitTask;
import org.mystic.utility.Misc;

public class IcyBonesAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(3) == 0) {
			e2.getUpdateFlags().sendGraphic(new Graphic(2598, 0, 0));
			TaskQueue.queue(new HitTask(2, false, new Hit(Misc.randomNumber(20)), e2));
		}
	}
}
