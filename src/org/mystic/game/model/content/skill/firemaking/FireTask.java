package org.mystic.game.model.content.skill.firemaking;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class FireTask extends Task {

	private final GameObject object;

	private final Player player;

	public FireTask(Player player, int cycles, GameObject object) {
		super(cycles, false, Task.StackType.STACK, Task.BreakType.NEVER, TaskIdentifier.CURRENT_ACTION);
		this.object = object;
		this.player = player;
		ObjectManager.register(object);
	}

	@Override
	public void execute() {
		ObjectManager.remove(object);
		GroundItemHandler.add(new Item(592, 1), object.getLocation(), player);
		stop();
	}

	@Override
	public void onStop() {

	}
}
