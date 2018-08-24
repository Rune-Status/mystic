package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class FlamesOfZamorakEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if ((Misc.randomNumber(4) == 0) && (p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player other = org.mystic.game.World.getPlayers()[e.getIndex()];

			if (other != null) {
				short[] tmp40_35 = other.getLevels();
				tmp40_35[6] = ((short) (int) (tmp40_35[6] - other.getLevels()[6] * 0.05D));

				if (other.getLevels()[6] < 0) {
					other.getLevels()[6] = 0;
				}

				other.getSkill().update(6);
			}
		}
	}
}
