package org.mystic.game.model.content.combat.special.effects;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class AncientMaceEffect implements CombatEffect2 {

	@Override
	public void execute(Player p, Entity e) {
		if (e.isNpc()) {
			return;
		}
		Player p2 = null;
		if (!e.isNpc()) {
			p2 = org.mystic.game.World.getPlayers()[e.getIndex()];
		}
		if (p2 != null) {
			p2.getSkill().getLevels()[Skills.PRAYER] = 0;
			p2.getPrayer().disable();
			p2.getClient().queueOutgoingPacket(
					new SendMessage("All of your prayer points have been drained by the ancient mace special attack!"));
			p.getClient().queueOutgoingPacket(new SendMessage("You drain all of your opponents prayer points."));
		}
	}
}