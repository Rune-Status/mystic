package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class DragonLongswordSpecialAttack implements Special {
	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 25;
	}

	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMelee().setAnimation(new Animation(1058, 0));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(248, 3));
	}
}
