package org.mystic.game.model.content.skill.agility.wilderness;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.task.Task;

public class SteppingStonesTask extends Task {

	private final Player player;

	private final int xMod;

	private final int yMod;

	private byte stage = 0;

	private final Controller start;

	private final int walk;

	private final boolean run;

	public SteppingStonesTask(Player player, Location dest) {
		super(player, 1, true);
		this.player = player;
		start = player.getController();
		this.walk = player.getAnimations().getWalkEmote();
		this.run = player.getRunEnergy().isRunning();
		player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
		int xDiff = player.getLocation().getX() - dest.getX();
		int yDiff = player.getLocation().getY() - dest.getY();
		if (xDiff != 0)
			xMod = (xDiff < 0 ? 1 : -1);
		else
			xMod = 0;
		if (yDiff != 0)
			yMod = (yDiff < 0 ? 1 : -1);
		else
			yMod = 0;
		if (xDiff != 0 && yDiff != 0) {
			player.setController(start);
			stop();
		}
		player.setTakeDamage(false);
	}

	@Override
	public void execute() {
		player.getRunEnergy().setRunning(false);
		player.setAppearanceUpdateRequired(true);
		player.getAnimations().setWalkEmote(769);
		if (stage == 0) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 1) {
			player.getMovementHandler().reset();
		} else if (stage == 3) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 4) {
			player.getMovementHandler().reset();
		} else if (stage == 5) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 6) {
			player.getMovementHandler().reset();
		} else if (stage == 7) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 8) {
			player.getMovementHandler().reset();
		} else if (stage == 9) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 10) {
			player.getMovementHandler().reset();
		} else if (stage == 12) {
			player.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 13) {
			stop();
		}
		stage++;
	}

	@Override
	public void onStop() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.getSkill().addExperience(Skills.AGILITY, 87);
		if (player.inWilderness()) {
			player.setController(ControllerManager.WILDERNESS_CONTROLLER);
		} else {
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
		}
		player.setTakeDamage(true);
		player.setAppearanceUpdateRequired(true);
	}

}