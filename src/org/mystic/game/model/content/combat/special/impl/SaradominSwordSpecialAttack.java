package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.impl.Attack;
import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class SaradominSwordSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player paramPlayer) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMagic().setAttack(new Attack(1, player.getCombat().getAttackCooldown()), null,
				new Graphic(1224, 0, true), new Graphic(1207, 0, true), null);
		player.getCombat().getMagic().execute(player.getCombat().getAttacking());
		player.getCombat().getMelee().setAnimation(new Animation(7072, 0));
	}

}
