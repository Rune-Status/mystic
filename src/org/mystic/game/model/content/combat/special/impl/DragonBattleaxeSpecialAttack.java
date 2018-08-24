package org.mystic.game.model.content.combat.special.impl;

import org.mystic.game.model.content.combat.special.Special;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.player.Player;

public class DragonBattleaxeSpecialAttack implements Special {

	@Override
	public boolean checkRequirements(Player player) {
		return false;
	}

	@Override
	public int getSpecialAmountRequired() {
		return 0;
	}

	@Override
	public void handleAttack(Player player) {
		SoundPlayer.play(player, Sounds.DRAGON_BAXE_SPEC);
	}
}
