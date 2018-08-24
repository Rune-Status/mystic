package org.mystic.game.model.content.skill.magic;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

public abstract class Spell {

	public abstract boolean execute(Player player);

	public abstract double getExperience();

	public abstract int getLevel();

	public abstract String getName();

	public abstract Item[] getRunes();

}
