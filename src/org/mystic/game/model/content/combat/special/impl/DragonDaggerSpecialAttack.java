package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;

public class DragonDaggerSpecialAttack implements Special {

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
		SoundPlayer.play(player, Sounds.DRAGON_DAGGER_SPEC);
		player.getCombat().getMelee().setAnimation(new Animation(1062, 0));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(252, 0));
		player.getCombat().getMelee().execute(player.getCombat().getAttacking());
	}
}
