package org.mystic.game.model.content.skill.woodcutting;

import org.mystic.cache.map.RSObject;
import org.mystic.cache.map.Region;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskIdentifier;

public class StumpTask extends Task {

	private GameObject object;

	private final int treeId;

	public StumpTask(GameObject object, int treeId, int delay) {
		super(delay, false, Task.StackType.STACK, Task.BreakType.NEVER, TaskIdentifier.NONE);
		this.treeId = treeId;
		this.object = object;
	}

	@Override
	public void execute() {
		ObjectManager.removeFromList(object);
		RSObject rsObject = new RSObject(object.getLocation().getX(), object.getLocation().getY(),
				object.getLocation().getZ(), treeId, 10, 0);
		Region.getRegion(object.getLocation().getX(), object.getLocation().getY()).addObject(rsObject);
		ObjectManager.send(new GameObject(treeId, object.getLocation().getX(), object.getLocation().getY(),
				object.getLocation().getZ(), 10, 0));
		stop();
	}

	@Override
	public void onStop() {
	}
}
