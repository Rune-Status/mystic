package org.mystic.game.task.impl;

import org.mystic.cache.map.RSObject;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.task.Task;
import org.mystic.game.task.impl.ObeliskTeleportation.Obelisk;

public class RegenerateObeliskTask extends Task {

	private final RSObject object;

	private final GameObject object2;

	private final Obelisk obelisk;

	private int regenerate;

	public RegenerateObeliskTask(RSObject object, GameObject object2, Obelisk obelisk, int delay, int regenerate) {
		super(delay);
		this.object = object;
		this.object2 = object2;
		this.obelisk = obelisk;
		this.regenerate = regenerate;
	}

	@Override
	public void execute() {
		if (regenerate == 8) {
			ObjectManager.register(object2);
		}
		if (regenerate == 0) {
			stop();
		}
		regenerate--;
	}

	@Override
	public void onStop() {

		// defines the new object
		GameObject restore = new GameObject(obelisk.getPillarId(), object.getX(), object.getY(), object.getZ(),
				object.getType(), object.getFace());

		// registers the new object
		ObjectManager.register(restore);

		// removes the past object
		ObjectManager.remove(object2);

	}

}