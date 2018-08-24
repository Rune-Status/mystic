package org.mystic.game.model.content.minigames.warriorsguild;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;

public class ConsumeTokens extends Task {

	private final Player player;

	private final Location NO_TOKENS = new Location(2846, 3540, 2);

	public ConsumeTokens(Player player, byte delay) {
		super(delay);
		this.player = player;
	}

	@Override
	public void execute() {
		if (player.getInventory().getItemAmount(8851) < 20) {
			player.teleport(NO_TOKENS);
			player.getClient().queueOutgoingPacket(new SendMessage("You have run out of tokens!"));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			stop();
		} else {
			player.getInventory().remove(8851, 20);
			player.getClient().queueOutgoingPacket(new SendMessage("20 of your tokens crumble to dust."));
		}
	}

	@Override
	public void onStop() {
	}
}
