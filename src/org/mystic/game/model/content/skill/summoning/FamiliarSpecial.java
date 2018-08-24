package org.mystic.game.model.content.skill.summoning;

import org.mystic.game.model.entity.player.Player;

public abstract interface FamiliarSpecial {

	public enum SpecialType {
		COMBAT, NONE;
	}

	public abstract boolean execute(Player paramPlayer, FamiliarMob paramFamiliarMob);

	public abstract int getAmount();

	public abstract double getExperience();

	public abstract SpecialType getSpecialType();
}
