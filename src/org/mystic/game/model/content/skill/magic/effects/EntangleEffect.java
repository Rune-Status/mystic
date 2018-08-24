package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class EntangleEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		e.getMovementHandler().reset();
		e.freeze(15, 5);
	}
}
