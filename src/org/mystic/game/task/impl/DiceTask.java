package org.mystic.game.task.impl;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.utility.Misc;

public class DiceTask extends Task {

	private Player player;

	private int ticks;

	private int roll;

	public DiceTask(Player player, int delay) {
		super(player, delay);
		this.player = player;
		player.getMovementHandler().reset();
		player.getFollowing().reset();
	}

	@Override
	public void execute() {
		if (ticks == 0) {
			roll = Misc.random(1, 100);
			player.getUpdateFlags().sendAnimation(new Animation(11900));
			player.getUpdateFlags().sendGraphic(new Graphic(2075));
			player.setDoingCape(true);
		}
		if (ticks == 2) {
			player.send(new SendMessage("You rolled @dre@" + roll + "@bla@ on the percentile dice."));
			if (player.getClan() != null) {
				player.getClan().sendMessage("Friends Chat channel-mate @dre@" + player.getUsername()
						+ "@bla@ rolled a @dre@" + roll + " @bla@on the percentile dice.");
			}
		}
		if (ticks == 4) {
			stop();
		}
		ticks++;
	}

	@Override
	public void onStop() {
		ticks = 0;
		player.setDoingCape(false);
		player.getSkill().lock(5);
	}

}