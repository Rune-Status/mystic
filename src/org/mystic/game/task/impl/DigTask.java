package org.mystic.game.task.impl;

import org.mystic.game.model.content.minigames.barrows.Barrows;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

/**
 * Digging with a spade
 */
public class DigTask extends Task {

	private final Player player;

	private int time = 0;

	public DigTask(Player player) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		player.getSkill().lock(3);
	}

	@Override
	public void execute() {
		if (time == 1) {
			player.getClient().queueOutgoingPacket(new SendMessage("You dig into the ground.."));
			player.getUpdateFlags().sendAnimation(830, 0);
			SoundPlayer.play(player, Sounds.DIG);
		} else if (time == 3) {
			if (Barrows.dig(player)) {
				stop();
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("...You find nothing of interest."));
				stop();
			}
		}
		time++;
	}

	@Override
	public void onStop() {
		time = 0;
	}

}
