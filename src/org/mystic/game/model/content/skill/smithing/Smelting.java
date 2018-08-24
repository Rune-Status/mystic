package org.mystic.game.model.content.skill.smithing;

import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.utility.Misc;

public class Smelting extends Task {

	public static final Animation SMELTING_ANIMATION = new Animation(899, 0);

	public static final String A = "You smelt ";

	public static final String B = ".";

	public static final String IRON_FAILURE = "You fail to refine the iron ore.";

	private final Player player;

	private final Bar data;

	private final int amount;

	private int smelted = 0;

	private final String name;

	public Smelting(Player player, int amount, Bar data) {
		super(player, 3, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.data = data;
		this.amount = amount;
		this.name = data.getResult().getDefinition().getName();
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		if (!canSmelt(player, data, false)) {
			stop();
		}
	}

	public boolean canSmelt(Player player, Bar data, boolean taskRunning) {
		if (player.getMaxLevels()[13] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Smithing level of " + data.getLevelRequired() + " to do this."));
			return false;
		}
		for (Item i : data.getRequiredOres()) {
			if (!player.getInventory().hasItemAmount(i.getId(), i.getAmount())) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage(taskRunning
								? "You have run out of " + i.getDefinition().getName() + "."
								: "You do not have any " + i.getDefinition().getName().toLowerCase() + " to smelt."));
				return false;
			}
		}
		return true;
	}

	@Override
	public void execute() {
		if (!canSmelt(player, data, true)) {
			stop();
			return;
		}
		player.getUpdateFlags().sendAnimation(SMELTING_ANIMATION);
		player.getInventory().remove(data.getRequiredOres(), false);
		if (data == Bar.IRON_BAR) {
			if (Skills.isSuccess(player, Skills.SMITHING, data.getLevelRequired())) {
				player.getInventory().add(data.getResult(), false);
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You smelt " + Misc.getAOrAn(name) + " " + name + "."));
			} else {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You ore is too impure and you fail to refine it."));
			}
		} else {
			player.getInventory().add(data.getResult(), false);
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You smelt " + Misc.getAOrAn(name) + " " + name + "."));
		}
		player.getInventory().update();
		player.getSkill().addExperience(Skills.SMITHING, data.getExp());
		if (++smelted == amount) {
			stop();
		}
	}

	@Override
	public void onStop() {
	}
}
