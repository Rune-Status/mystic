package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class UnholyCurseBearerAbility implements CombatEffect {

	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(3) == 0) {
			Player player = null;
			if (!e2.isNpc()) {
				player = org.mystic.game.World.getPlayers()[e2.getIndex()];
			}
			e2.getUpdateFlags().sendGraphic(new Graphic(2440, 0, 100));
			for (int i = 0; i <= 6; i++)
				if (i != 3) {
					int val = i;
					short[] t_val = e2.getLevels();
					t_val[val] = ((short) (t_val[val] - 1));
					if (e2.getLevels()[i] < 0) {
						e2.getLevels()[i] = 0;
					}
					if (player != null) {
						player.getSkill().update(i);
					}
				}
		}
	}
}
