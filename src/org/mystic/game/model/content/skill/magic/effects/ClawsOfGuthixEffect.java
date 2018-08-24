package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class ClawsOfGuthixEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		if ((Misc.randomNumber(4) == 0) && (p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player other = org.mystic.game.World.getPlayers()[e.getIndex()];

			if (other != null) {
				int tmp39_38 = 1;
				short[] tmp39_35 = other.getLevels();
				tmp39_35[tmp39_38] = ((short) (int) (tmp39_35[tmp39_38] - other.getLevels()[1] * 0.05D));

				if (other.getLevels()[1] < 0) {
					other.getLevels()[1] = 0;
				}

				other.getSkill().update(1);
			}
		}
	}
}
