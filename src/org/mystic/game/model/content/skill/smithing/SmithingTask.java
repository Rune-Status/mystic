package org.mystic.game.model.content.skill.smithing;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.content.sound.SoundPlayer;
import org.mystic.game.model.content.sound.SoundPlayer.Sounds;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class SmithingTask extends Task {

	public static void start(Player p, int item, int amount, int interfaceId, int slot) {
		String check = Item.getDefinition(item).getName().substring(0, 3);
		int bar = -1;
		int make = 1;
		if (Item.getDefinition(item).isStackable()) {
			make = 15;
		}
		switch (check) {
		case "Bro":
			bar = SmithingConstants.BARS[0];
			break;
		case "Iro":
			bar = SmithingConstants.BARS[1];
			break;
		case "Ste":
			bar = SmithingConstants.BARS[2];
			break;
		case "Mit":
			bar = SmithingConstants.BARS[3];
			break;
		case "Ada":
			bar = SmithingConstants.BARS[4];
			break;
		case "Run":
			bar = SmithingConstants.BARS[5];
			break;
		}
		TaskQueue.queue(new SmithingTask(p, new Item(item, make),
				new Item(bar, SmithingConstants.getBarAmount(interfaceId, slot)), amount));
	}

	private final Player player;
	private final Item smith;
	private final Item bar;
	private final int amount;

	private int loop = 0;

	public SmithingTask(Player player, Item smith, Item bar, int amount) {
		super(player, 2, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.smith = smith;
		this.bar = bar;
		this.amount = amount;
		int lvl = SmithingConstants.getLevel(smith.getId());
		if (player.getMaxLevels()[13] < lvl) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You need a Smithing level of " + lvl + " to make that."));
			stop();
		} else if (!player.getInventory().hasItemAmount(new Item(bar))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough bars to make that."));
			stop();
		} else if (!player.getInventory().hasItemId(2347)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a hammer to smith this."));
			stop();
		} else {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}
	}

	@Override
	public void execute() {
		if (!hasRequirements()) {
			stop();
			return;
		}
		SoundPlayer.play(player, Sounds.SMITH);
		player.getSkill().addExperience(Skills.SMITHING, getExperience());
		player.getInventory().remove(new Item(bar), false);
		player.getInventory().add(new Item(smith), true);
		player.getUpdateFlags().sendAnimation(898, 0);
		player.getClient()
				.queueOutgoingPacket(new SendMessage("You hammer the " + bar.getDefinition().getName().toLowerCase()
						+ " into an " + smith.getDefinition().getName().toLowerCase() + "."));
		if (++loop == amount) {
			stop();
		}
	}

	public double getExperience() {
		switch (bar.getId()) {
		case 2349:
			return 30 * bar.getAmount();
		case 2351:
			return 75 * bar.getAmount();
		case 2353:
			return 90 * bar.getAmount();
		case 2359:
			return 120 * bar.getAmount();
		case 2361:
			return 200 * bar.getAmount();
		case 2363:
			return 400 * bar.getAmount();
		}
		return 10;
	}

	public boolean hasRequirements() {
		if (!player.getInventory().hasItemAmount(new Item(bar))) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You have run out of " + bar.getDefinition().getName().toLowerCase() + "s to smith."));
			return false;
		}
		if (!player.getInventory().hasItemId(2347)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You don't have a hammer to smith with."));
			return false;
		}
		return true;
	}

	@Override
	public void onStop() {
	}
}
