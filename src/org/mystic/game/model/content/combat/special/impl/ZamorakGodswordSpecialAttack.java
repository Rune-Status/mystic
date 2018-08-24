package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class ZamorakGodswordSpecialAttack implements Special {

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
		player.getCombat().getMelee().setAnimation(new Animation(7070, 0));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(2110, 0));
		player.getCombat().getAttacking().getUpdateFlags().sendGraphic(Graphic.lowGraphic(2111, 0));
	}
}
