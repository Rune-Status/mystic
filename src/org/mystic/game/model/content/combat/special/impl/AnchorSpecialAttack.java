package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class AnchorSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public void handleAttack(Player player) {

		player.getCombat().getMelee().setAnimation(new Animation(5870, 0));

		player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(1027, 0));
	}
}
