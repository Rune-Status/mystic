package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class SeercullEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		int drain = (int) (p.getLastDamageDealt() * 0.1D);
		if (drain == 0) {
			return;
		}
		short[] tmp22_17 = e.getLevels();
		tmp22_17[6] = ((short) (tmp22_17[6] - drain));
		if (e.getLevels()[6] < 0) {
			e.getLevels()[6] = 0;
		}
		if (!e.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			p2.getSkill().update(6);
		}
	}
}
