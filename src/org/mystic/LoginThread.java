package org.mystic;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.Client;
import org.mystic.game.model.networking.StreamBuffer;
import org.mystic.game.model.player.io.PlayerSave;

public class LoginThread extends Thread {

	private static Logger logger = Logger.getLogger(Engine.class.getSimpleName());

	private static final Queue<Player> login = new ConcurrentLinkedQueue<Player>();

	public static void cycle() {
		long start = System.currentTimeMillis();
		Player player = null;
		if ((player = login.poll()) != null) {
			logger.info("Logging in: " + player.getUsername());
			boolean starter = false;
			boolean wasLoaded = false;
			try {
				starter = !PlayerSave.load(player);
				wasLoaded = true;
			} catch (Exception e) {
				if (player != null) {
					StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
					resp.writeByte(11);
					resp.writeByte(0);
					resp.writeByte(0);
					player.getClient().send(resp.getBuffer());
				}
				e.printStackTrace();
			}
			if (wasLoaded) {
				try {
					boolean login = player.login(starter);
					if (login) {
						player.getClient().setStage(Client.Stages.LOGGED_IN);
					}
				} catch (Exception e) {
					e.printStackTrace();
					player.logout(true);
				}
			}

		}
		long elapsed = System.currentTimeMillis() - start;
		if (elapsed < 500L) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Login thread overflow: " + elapsed);
		}
	}

	public static void queueLogin(Player player) {
		login.add(player);
	}

	public LoginThread() {
		setName("Login Thread");
		setPriority(Thread.MAX_PRIORITY - 2);
		start();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			cycle();
		}
	}
}
