package org.mystic.game.model.content.skill.magic.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.content.skill.prayer.PrayerConstants;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class TeleBlockEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (!e.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			if (p2.getPrayer().active(PrayerConstants.PROTECT_FROM_MAGIC)) {
				p2.getClient().queueOutgoingPacket(new SendMessage(
						"<col=81006F>A teleblock spell has been cast on you. It will expire in 2 minutes, 30 seconds.</col>"));
				e.teleblock(150);
				return;
			} else {
				p2.getClient().queueOutgoingPacket(new SendMessage(
						"<col=81006F>A teleblock spell has been cast on you. It will expire in 5 minutes, 0 seconds.</col>"));
				e.teleblock(300);
				return;
			}
		} else {
			e.teleblock(300);
			return;
		}
	}
}
