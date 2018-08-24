package org.mystic;

import org.mystic.game.World;
import org.mystic.game.model.entity.object.GameObject;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.entity.player.Player;

public class NetworkThread extends Thread {

	public static final String PACKET_LOG_DIR = "./data/logs/packets/";

	private static GameObject object;

	public static void cycle() {
		long start = System.nanoTime();
		while ((object = ObjectManager.getSend().poll()) != null) {
			try {
				ObjectManager.send(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Player player : World.getPlayers()) {
			try {
				if ((player != null) && (player.isActive())) {
					try {
						if (player.getPlayerShop().hasSearch()) {
							player.getPlayerShop().doSearch();
							player.getPlayerShop().resetSearch();
						}
						player.getGroundItems().process();
						player.getObjects().process();
						player.getClient().processOutgoingPackets();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long elapsed = (System.nanoTime() - start) / 1000000;
		if (elapsed < 200) {
			try {
				Thread.sleep(200 - elapsed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Network thread overflow: " + elapsed);
		}
	}

	public NetworkThread() {
		setName("Network Thread");
		setPriority(Thread.MAX_PRIORITY - 1);
		start();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			cycle();
		}
	}

}
