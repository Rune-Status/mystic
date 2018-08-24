package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.Controller;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.task.Task;

public class JumpObjectTask extends Task {

	private final Player player;

	private final Location dest;

	private final Controller start;

	public JumpObjectTask(Player player, Location dest) {
		super(player, 1);
		this.player = player;
		this.dest = dest;
		this.start = player.getController();
		player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
	}

	@Override
	public void execute() {
		stop();
	}

	@Override
	public void onStop() {
		player.teleport(dest);
		player.setController(start);
	}

}
