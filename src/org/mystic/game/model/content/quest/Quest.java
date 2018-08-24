package org.mystic.game.model.content.quest;

import org.mystic.game.model.entity.player.Player;

public abstract interface Quest {

	public abstract void init(Player player);

	public abstract void doAction(Player player, int stage);

	public abstract void reward(Player player);

	public abstract String getName();

	public abstract byte getFinalStage();

	public abstract short getId();

	public abstract boolean useItemOnObject(Player player, int param, int param2);

	public abstract boolean clickObject(Player player, int param, int param2);

	public abstract boolean clickButton(Player player, int param);

	public abstract byte getPoints();

	public abstract String[] getLinesForStage(byte param);
}
