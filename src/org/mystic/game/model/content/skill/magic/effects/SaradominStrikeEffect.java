package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class SaradominStrikeEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if ((p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player other = org.mystic.game.World.getPlayers()[e.getIndex()];

			if (other != null) {
				int tmp32_31 = 5;
				short[] tmp32_28 = other.getLevels();
				tmp32_28[tmp32_31] = ((short) (tmp32_28[tmp32_31] - 1));

				if (other.getLevels()[5] < 0) {
					other.getLevels()[5] = 0;
				}

				other.getSkill().update(5);
			}
		}
	}
}
