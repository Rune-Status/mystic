package org.mystic.game.model.content.skill.herblore;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class CleanHerbTask extends Task {

	private boolean meetsRequirements(Player player, GrimyHerbData data) {
		if (player.getSkill().getLevels()[15] < data.getLevelReq()) {
			player.getClient().queueOutgoingPacket(new SendMessage(
					"You need a Herblore level of atleast " + data.getLevelReq() + " to clean this herb."));
			return false;
		}
		return true;
	}

	private final Player player;

	private int slot;

	private final GrimyHerbData data;

	public CleanHerbTask(Player player, int slot, GrimyHerbData data) {
		super(player, 0, true, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.slot = slot;
		this.data = data;
	}

	private void cleanHerb() {
		player.getInventory().getItems()[slot] = null;
		player.getInventory().getItems()[slot] = new Item(data.getCleanHerb(), 1);
		player.getInventory().update();
		player.getSkill().addExperience(15, data.getExp());
		player.getClient().queueOutgoingPacket(new SendMessage("You clean the dirt off the herb."));
	}

	@Override
	public void execute() {
		if (meetsRequirements(player, data)) {
			cleanHerb();
		}
		stop();
	}

	@Override
	public void onStop() {
	}
}