package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class KalGerTheWarmongerAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(10) == 0) {
			Player p = null;

			if (!e2.isNpc()) {
				p = org.mystic.game.World.getPlayers()[e2.getIndex()];
			}

			for (int i = 0; i <= 6; i++)
				if (i != 3) {
					if (e2.getLevels()[i] > e2.getMaxLevels()[i] * 0.7D) {
						int tmp71_69 = i;
						short[] tmp71_66 = e2.getLevels();
						tmp71_66[tmp71_69] = ((short) (tmp71_66[tmp71_69] - 2));

						if (p != null)
							p.getSkill().update(i);
					}
				}
		}
	}
}
