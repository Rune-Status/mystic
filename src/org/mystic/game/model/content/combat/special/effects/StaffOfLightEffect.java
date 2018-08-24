package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class StaffOfLightEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (p.getAttributes().get("staffOfLightEffect") != null) {
			if (System.currentTimeMillis()
					- ((Long) p.getAttributes().get("staffOfLightEffect")).longValue() < 420000L) {
				p.getClient().queueOutgoingPacket(new SendMessage("You are already protected."));
				return;
			}
			p.getAttributes().remove("staffOfLightEffect");
			p.getAttributes().set("staffOfLightEffect", Long.valueOf(System.currentTimeMillis()));
			p.getClient()
					.queueOutgoingPacket(new SendMessage("Spirits of deceased evildoers offer you their protection."));
		}
	}
}
