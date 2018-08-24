package org.mystic.game.model.entity.item.impl;

import java.util.ArrayList;
import java.util.List;

import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;

public class GlobalItemHandler {

	private static final List<GroundItem> groundItems = new ArrayList<GroundItem>();

	private static void add(GroundItem item) {
		groundItems.add(item);
	}

	public static void respawn(final GroundItem groundItem) {
		final GroundItem tempItem = new GroundItem(groundItem.getItem(), groundItem.getLocation(),
				groundItem.getRespawnTimer());
		TaskQueue.queue(new Task(tempItem.getRespawnTimer()) {
			@Override
			public void execute() {
				GroundItemHandler.add(tempItem);
				GroundItemHandler.globalize(tempItem);
				this.stop();
			}

			@Override
			public void onStop() {

			}

		});
	}

	public static void spawn() {

		add(new GroundItem(new Item(952, 1), new Location(3563, 3305), 10));

		for (GroundItem item : groundItems) {
			if (item == null) {
				continue;
			}
			GroundItemHandler.add(item);
		}
	}

}