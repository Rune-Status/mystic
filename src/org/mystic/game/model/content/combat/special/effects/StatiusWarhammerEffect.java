package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class StatiusWarhammerEffect implements CombatEffect2 {
	@Override
	public void execute(Player p, Entity e) {
		int decrease = (int) (e.getLevels()[1] * 0.3D);
		int tmp18_17 = 1;
		short[] tmp18_14 = e.getLevels();
		tmp18_14[tmp18_17] = ((short) (tmp18_14[tmp18_17] - decrease));
		if (e.getLevels()[1] < 0) {
			e.getLevels()[1] = 0;
		}

		if (!e.isNpc()) {
			org.mystic.game.World.getPlayers()[e.getIndex()].getSkill().update(1);
		}

		p.getClient().queueOutgoingPacket(new SendMessage("You decrease your opponent's Defence level by 30%."));
	}
}
