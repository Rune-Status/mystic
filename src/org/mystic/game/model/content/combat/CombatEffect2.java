package org.mystic.game.model.content.combat;

import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public abstract interface CombatEffect2 {

	public abstract void execute(Player paramPlayer, Entity paramEntity);
}
