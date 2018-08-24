package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class DragonScimitarEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if ((p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player attacked = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (attacked == null) {
				return;
			}
			attacked.getPrayer().disable(9);
		}
	}
}
