package org.mystic.game.model.content.combat;

import org.mystic.game.model.entity.Entity;

public abstract interface CombatEffect {

	public abstract void execute(Entity paramEntity1, Entity paramEntity2);
}
