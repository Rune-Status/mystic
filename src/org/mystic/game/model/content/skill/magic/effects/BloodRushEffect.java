package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class BloodRushEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		int dmg = p.getLastDamageDealt();
		if (dmg >= 4) {
			int heal = dmg / 4;
			int tmp20_19 = 3;
			short[] tmp20_16 = p.getLevels();
			tmp20_16[tmp20_19] = ((short) (tmp20_16[tmp20_19] + heal));
			if (p.getLevels()[3] > p.getMaxLevels()[3]) {
				p.getLevels()[3] = p.getMaxLevels()[3];
			}
			p.getSkill().update(3);
			p.send(new SendMessage(PlayerConstants.ABSORB_HEALTH_MESSAGE));
		}
	}
}
