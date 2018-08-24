package org.mystic.game;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.mystic.LoginThread;
import org.mystic.NetworkThread;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.object.ObjectManager;
import org.mystic.game.model.networking.PipelineFactory;
import org.mystic.game.task.TaskQueue;

public class GameThread extends Thread {

	/** The logger for printing information. */
	private static Logger logger = Logger.getLogger(GameThread.class.getSimpleName());

	public static void init() {
		try {
			try {
				startup();
			} catch (Exception e) {
				e.printStackTrace();
			}
			new GameThread();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the server up.
	 * 
	 * @throws Exception
	 */
	private static void startup() throws Exception {
		long startTime = System.currentTimeMillis();
		if (!GameConstants.DEV_MODE) {
			System.setErr(new PrintStream(new FileOutputStream("./data/logs/err.txt", true)));
		}
		GameDataLoader.load();
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory());
		new LoginThread();
		new NetworkThread();
		while (true) {
			try {
				serverBootstrap.bind(new InetSocketAddress(43594));
				break;
			} catch (ChannelException e2) {
				logger.info("Error: The server could not bind port - sleeping..");
				Thread.sleep(2000);
			}
		}

		logger.info("Mystic has successfully loaded in " + Math.round((System.currentTimeMillis() - startTime) / 1000)
				+ " seconds.");
	}

	/**
	 * Create the game thread
	 */
	private GameThread() {
		setName("Main Thread");
		setPriority(Thread.MAX_PRIORITY);
		start();
	}

	/**
	 * Performs a server cycle.
	 */
	private void cycle() {
		try {
			TaskQueue.process();
			GroundItemHandler.process();
			ObjectManager.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			World.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				long s = System.nanoTime();
				cycle();
				long e = (System.nanoTime() - s) / 1000000;
				if (e < 600) {
					if (e < 400) {
						for (int i = 0; i < 30; i++) {
							long sleep = (600 - e) / 30;
							Thread.sleep(sleep);
						}
					} else {
						Thread.sleep(600 - e);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}