package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class ZamorakGodswordEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (!e.isNpc()) {
			Player p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			p2.freeze(20, 5);
			p2.getClient().queueOutgoingPacket(new SendMessage("You have been frozen!"));
		}
	}
}