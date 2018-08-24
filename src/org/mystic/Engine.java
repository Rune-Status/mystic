package org.mystic;

import java.util.logging.Logger;

import org.mystic.game.GameConstants;
import org.mystic.game.GameThread;
import org.mystic.game.World;
import org.mystic.game.model.content.clanchat.ClanManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.player.io.PlayerSave;

public class Engine {

	private static final Logger logger = Logger.getLogger(Engine.class.getSimpleName());

	public static final ClanManager clanManager = new ClanManager();

	public static void main(String[] args) {
		logger.info("Development mode: " + (GameConstants.DEV_MODE ? "ACTIVE" : "OFF") + ".");
		if (!GameConstants.DEV_MODE) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				for (Player players : World.getPlayers()) {
					if (players != null && players.isActive()) {
						PlayerSave.save(players);
					}
				}
			}));
		}
		GameThread.init();
	}
}