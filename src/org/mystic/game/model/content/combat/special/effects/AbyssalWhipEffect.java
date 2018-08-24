package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class AbyssalWhipEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (!e.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			if (p2.getRunEnergy().getEnergy() >= 4) {
				int absorb = (int) (p2.getRunEnergy().getEnergy() * 0.25D);
				p2.getRunEnergy().deduct(absorb);
				p.getRunEnergy().add(absorb);
				p.getClient().queueOutgoingPacket(new SendMessage("You absorb 25% of your opponents run energy."));
				p2.getClient().queueOutgoingPacket(new SendMessage("25% of your run energy has been absorbed!"));
			}
		}
	}
}
