package org.mystic.game.model.entity.object;

import java.util.Deque;
import java.util.LinkedList;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendObject;

public class LocalObjects {

	private final Player player;

	private Deque<GameObject> adding = new LinkedList<GameObject>();

	private boolean load = false;

	public LocalObjects(Player player) {
		this.player = player;
	}

	public void add(GameObject o) {
		adding.add(o);
	}

	private void load() {
		if (ObjectManager.getActive() == null) {
			return;
		}
		if (ObjectManager.getActive() == null) {
			return;
		}
		for (GameObject o : ObjectManager.getActive()) {
			if (player.withinRegion(o.getLocation()) && player.getLocation().getZ() % 4 == o.getLocation().getZ() % 4) {
				player.getClient().queueOutgoingPacket(new SendObject(player, o));
			}
		}
		load = false;
	}

	public void onRegionChange() {
		adding.clear();
		load = true;
	}

	public void process() {
		if (load) {
			load();
		}
		GameObject g = null;
		while ((g = adding.poll()) != null) {
			player.getClient().queueOutgoingPacket(new SendObject(player, g));
		}
	}

}