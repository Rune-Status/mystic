package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class SmokeRushEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		if ((p.getLastDamageDealt() >= 0) && (Misc.randomNumber(2) == 0))
			e.poison(2);
	}
}
