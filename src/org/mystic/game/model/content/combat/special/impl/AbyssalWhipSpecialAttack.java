package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class AbyssalWhipSpecialAttack implements Special {

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
		SoundPlayer.play(player, Sounds.WHIP_SPEC);
		player.getCombat().getAttacking().getUpdateFlags().sendGraphic(Graphic.highGraphic(2108, 0));
		player.getCombat().getMelee().setAnimation(new Animation(1658, 0));
	}
}
