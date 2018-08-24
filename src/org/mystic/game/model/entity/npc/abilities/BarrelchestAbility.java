package org.mystic.game.model.entity.npc.abilities;

import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.content.skill.prayer.PrayerBook;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.Misc;

public class BarrelchestAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getLastDamageDealt() <= 0) {
			return;
		}
		if ((e2.getLevels()[5] > 0) && (!e2.isNpc())) {
			final Player player = org.mystic.game.World.getPlayers()[e2.getIndex()];
			if (player != null) {
				player.getPrayer().drain(10 + Misc.randomNumber(10));
				if ((player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT)
						&& (player.getPrayer().active(18))) {
					player.getPrayer().disable(18);
				}
			}
		}
	}
}
