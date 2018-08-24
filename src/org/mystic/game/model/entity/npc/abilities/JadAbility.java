package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;

public class JadAbility implements CombatEffect {

	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getCombat().getCombatType() == CombatTypes.RANGED) {
			e2.getUpdateFlags().sendGraphic(new Graphic(451, 0, 0));
		}
	}

}
