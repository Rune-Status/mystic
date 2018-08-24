package org.mystic.game.model.content.wilderness;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.utility.Misc;

public class EPTask extends Task {

	private final Player player;

	public EPTask(final Player player, final byte delay) {
		super(delay);
		this.player = player;
	}

	@Override
	public void execute() {
		if (!player.inWilderness() || player.getEP() >= 100) {
			stop();
			return;
		}
		if (player.inWilderness()) {
			if (player.epTimer >= TargetConstants.EP_TIME) {
				player.setEP(player.getEP() + Misc.randomNumber(23));
				player.send(new SendMessage("Your EP has increased to " + player.getEP() + "%."));
				if (player.getEP() > 100) {
					player.setEP(100);
					stop();
					return;
				}
				player.epTimer = 0;
			}
			player.epTimer++;
		}
	}

	@Override
	public void onStop() {
		player.getAttributes().remove("gainEP");
	}
}