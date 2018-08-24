package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class ShadowBlitzEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		if (p.getLastDamageDealt() > -1) {
			int tmp13_12 = 0;
			short[] tmp13_9 = e.getLevels();
			tmp13_9[tmp13_12] = ((short) (int) (tmp13_9[tmp13_12] - e.getLevels()[0] * 0.5D));
			if (e.getLevels()[0] < 0) {
				e.getLevels()[0] = 0;
			}

			if (!e.isNpc()) {
				Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];

				if (p2 == null) {
					return;
				}

				p2.getSkill().update(0);
			}
		}
	}
}
