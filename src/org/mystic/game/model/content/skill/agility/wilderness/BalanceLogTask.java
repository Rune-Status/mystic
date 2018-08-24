package org.mystic.game.model.content.skill.agility.wilderness;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.TaskQueue;
import org.mystic.game.task.impl.ForceMovementTask;

public class BalanceLogTask extends ForceMovementTask {

	private final int walk;

	private final boolean run;

	private final double exp;

	public BalanceLogTask(Player player, Location l, double exp) {
		super(player, l, ControllerManager.FORCE_MOVEMENT_CONTROLLER);
		walk = player.getAnimations().getWalkEmote();
		run = player.getRunEnergy().isRunning();
		this.exp = exp;
		if (!stopped()) {
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(762);
			player.getRunEnergy().setRunning(false);
		}
		player.setTakeDamage(false);
		player.getClient().queueOutgoingPacket(new SendMessage("You balance and walk across the slippery log.."));
	}

	@Override
	public void onDestination() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(16, exp);
		player.setTakeDamage(true);
	}

	@Override
	public void onStop() {
		TaskQueue.queue(new GateDoorEnter2(player, 2998, 3931, 0, new Location(2998, 3931)));
	}
}