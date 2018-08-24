package org.mystic.game.model.content.skill.herblore;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;
import org.mystic.game.task.TaskQueue;

public class HerbloreGrindingTask extends Task {

	public static void handleGrindingIngredients(Player player, Item used, Item usedWith) {
		int itemId = used.getId() != 233 ? used.getId() : usedWith.getId();
		GrindingData data = GrindingData.forId(itemId);
		if (data == null) {
			return;
		}
		player.getUpdateFlags().sendAnimation(new Animation(364));
		TaskQueue.queue(new HerbloreGrindingTask(player, data));
	}

	private final Player player;

	private final GrindingData data;

	public HerbloreGrindingTask(Player player, GrindingData data) {
		super(player, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, TaskIdentifier.CURRENT_ACTION);
		this.player = player;
		this.data = data;
	}

	private void create() {
		player.getInventory().remove(data.getItemId(), 1);
		player.getInventory().add(new Item(data.getGroundId(), 1));
	}

	@Override
	public void execute() {
		create();
		stop();
	}

	@Override
	public void onStop() {
	}
}
