package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class ShearSheepTask extends Task {

	private final Player player;

	private int time;

	public ShearSheepTask(Player player, int time) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.time = time;
	}

	@Override
	public void execute() {
		if (time == 6) {
			player.setDoingCape(true);
			player.getUpdateFlags().sendAnimation(new Animation(893));
		}
		if (time == 0) {
			stop();
		}
		time--;
	}

	@Override
	public void onStop() {
		player.setDoingCape(false);
	}

}