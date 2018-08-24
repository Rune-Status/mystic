package org.mystic.game.model.content.skill.farming;

import java.util.Calendar;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.Misc;

public class GrassyPatch {

	private byte stage = 0;

	private int minute, hour, day, year;

	public void click(Player player, int option, int index) {
		if (option == 1) {
			rake(player, index);
		}
	}

	public void doConfig(Player p, int index) {
		p.getFarming().doConfig();
	}

	public int getConfig(int index) {
		return stage * FarmingPatches.values()[index].mod;
	}

	public boolean isRaked() {
		return stage == 3;
	}

	public void process(Player player, int index) {
		if (stage == 0) {
			return;
		}
		int elapsed = Misc.getMinutesElapsed(minute, hour, day, year);
		int grow = 4;
		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				if (stage == 0) {
					return;
				}
				stage = ((byte) (stage - 1));
			}
			setTime();
		}
	}

	public void rake(final Player p, final int index) {
		if (isRaked()) {
			p.getClient().queueOutgoingPacket(new SendMessage("This plot is already fully raked."));
			return;
		}
		if (!p.getInventory().hasItemId(5341)) {
			p.getClient().queueOutgoingPacket(new SendMessage("You need a rake to do this."));
			return;
		}
		p.getUpdateFlags().sendAnimation(2273, 0);
		TaskQueue.queue(new Task(p, 4, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE,
				TaskIdentifier.CURRENT_ACTION) {
			@Override
			public void execute() {
				setTime();
				GrassyPatch patch = GrassyPatch.this;
				patch.stage = ((byte) (patch.stage + 1));
				doConfig(p, index);
				p.getUpdateFlags().sendAnimation(2273, 0);
				p.getSkill().addExperience(Skills.FARMING, 20.0D);
				p.getInventory().addOrCreateGroundItem(6055, 1, true);
				if (isRaked()) {
					p.getClient().queueOutgoingPacket(new SendMessage("The plot is now fully raked."));
					p.getUpdateFlags().sendAnimation(65535, 0);
					stop();
					return;
				}
			}

			@Override
			public void onStop() {

			}
		});
	}

	public void setTime() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(11);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

}