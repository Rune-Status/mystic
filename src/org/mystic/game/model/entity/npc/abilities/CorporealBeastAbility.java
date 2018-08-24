package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class CorporealBeastAbility implements CombatEffect {

	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getCombat().getCombatType() == CombatTypes.MELEE) {
			Npc mob = org.mystic.game.World.getNpcs()[e1.getIndex()];
			if ((mob != null) && (mob.getCombatIndex() == 0))
				for (Player p : mob.getCombatants())
					if (p != e2) {
						mob.getCombat().getMelee().finish(p,
								new Hit(Misc.randomNumber(e1.getMaxHit(CombatTypes.MELEE)), Hit.HitTypes.MELEE));
					}
		}
	}
}
