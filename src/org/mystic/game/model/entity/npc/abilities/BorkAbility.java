package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;

public class BorkAbility implements CombatEffect {

	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.isNpc()) {
			Npc m = org.mystic.game.World.getNpcs()[e1.getIndex()];
			if ((m != null) && (m.getCombat().getCombatType() == CombatTypes.MAGIC) && (m.getCombatants() != null)
					&& (m.getCombatants().size() > 0)) {
				for (Player p : m.getCombatants()) {
					if (!p.equals(e1.getCombat().getAttacking())) {
						m.getCombat().getMagic().finish(p);
					}
				}
			}
		}
	}
}
