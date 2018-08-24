package org.mystic.game.model.content.skill.agility.gnome;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.task.impl.ForceMovementTask;

public class BalanceOverObject extends ForceMovementTask {

	private final int walk;

	private final boolean run;

	private final double exp;

	public BalanceOverObject(Player player, Location l, double exp) {
		super(player, l, ControllerManager.DEFAULT_CONTROLLER);
		this.walk = player.getAnimations().getWalkEmote();
		this.run = player.getRunEnergy().isRunning();
		this.exp = exp;
		if (!stopped()) {
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(762);
			player.getRunEnergy().setRunning(false);
		}
		player.setTakeDamage(false);
	}

	@Override
	public void onDestination() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(Skills.AGILITY, exp);
		player.setTakeDamage(true);
	}
}
