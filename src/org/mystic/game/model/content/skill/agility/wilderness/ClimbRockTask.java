package org.mystic.game.model.content.skill.agility.wilderness;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.skill.agility.Agility;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.impl.ForceMovementTask;

public class ClimbRockTask extends ForceMovementTask {

	private final int walk;

	private final boolean run;

	private final double exp;

	public ClimbRockTask(Player player, Location l, double exp) {
		super(player, l, ControllerManager.FORCE_MOVEMENT_CONTROLLER);
		walk = player.getAnimations().getWalkEmote();
		run = player.getRunEnergy().isRunning();
		this.exp = exp;
		if (!stopped()) {
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(740);
			player.getRunEnergy().setRunning(false);
		}
	}

	@Override
	public void onDestination() {
		stop();
	}

	@Override
	public void onStop() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(Skills.AGILITY, exp);
		player.setController(ControllerManager.WILDERNESS_CONTROLLER);
		player.setTakeDamage(true);
		player.getClient().queueOutgoingPacket(new SendMessage("You climb up the pile of rocks."));
		Agility.finishWild(player);
	}

}