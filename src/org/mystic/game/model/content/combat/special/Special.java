package org.mystic.game.model.content.combat.special;

import org.mystic.game.model.entity.player.Player;

public abstract interface Special {

	public abstract boolean checkRequirements(Player player);

	public abstract int getSpecialAmountRequired();

	public abstract void handleAttack(Player player);

}
