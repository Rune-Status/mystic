package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class DragonSpearSpecialAttack implements Special {

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
		SoundPlayer.play(player, Sounds.DRAGON_SPEAR_SPEC);
		player.getCombat().getAttacking().getUpdateFlags().sendGraphic(Graphic.highGraphic(80, 0));
		player.getCombat().getMelee().setAnimation(new Animation(1064, 0));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(253, 0));
	}
}
