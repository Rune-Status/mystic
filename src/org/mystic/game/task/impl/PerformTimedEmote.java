package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class PerformTimedEmote extends Task {

	private final Player player;

	private Animation animation;

	private Graphic graphic;

	private int time = 0;

	public PerformTimedEmote(Player player, int time, Animation animation, Graphic graphic) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.time = time;
		this.animation = animation;
		this.graphic = graphic;
	}

	@Override
	public void execute() {
		if (time == 6) {
			player.setDoingCape(true);
			player.getUpdateFlags().sendAnimation(animation);
			player.getUpdateFlags().sendGraphic(graphic);
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
