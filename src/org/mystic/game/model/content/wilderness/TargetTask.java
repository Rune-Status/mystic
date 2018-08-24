package org.mystic.game.model.content.wilderness;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;

public class TargetTask extends Task {

	private final Player player;

	public TargetTask(final Player player, final byte delay) {
		super(delay);
		this.player = player;
	}

	@Override
	public void execute() {
		if (!player.inWilderness() || player.getTarget() != null) {
			stop();
			return;
		}
		if (player.inWilderness()) {
			if (player.targetTimer >= TargetConstants.TARGET_TIME) {
				WildernessTargets.findTarget(player);
				if (player.getTarget() != null) {
					stop();
					return;
				}
			}
			player.targetTimer++;
		}
	}

	@Override
	public void onStop() {
		player.getAttributes().remove("gainTarget");
		player.targetTimer = 0;
	}
}