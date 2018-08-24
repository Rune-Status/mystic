package org.mystic.game.model.content.minigames.pestcontrol;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mystic.game.World;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class PestControl {

	private static final List<PestControlGame> games = new LinkedList<PestControlGame>();

	private static final Queue<Player> waiting = new ArrayDeque<Player>();

	private static short time = 1;

	public static boolean clickObject(Player player, int id) {
		switch (id) {

		case 14315:
			if (player.getSummoning().hasFamiliar()) {
				player.send(new SendMessage("You can't bring familiars onto the boats."));
			} else {
				if (!player.getController().equals(ControllerManager.PEST_WAITING_ROOM_CONTROLLER)) {
					player.setController(ControllerManager.PEST_WAITING_ROOM_CONTROLLER);
					player.getUpdateFlags().sendAnimation(new Animation(828));
					player.teleport(new Location(2661, 2639));
					if (!waiting.contains(player)) {
						waiting.add(player);
					}
				}
			}
			return true;

		case 14314:
			if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
				player.setController(ControllerManager.DEFAULT_CONTROLLER);
				player.teleport(new Location(2657, 2639));
				player.getUpdateFlags().sendAnimation(new Animation(828));
				waiting.remove(player);
			}
			return true;
		}
		return false;

	}

	public static int getMinutesTillDepart() {
		return time / 100;
	}

	public static int getPlayersReady() {
		return waiting.size();
	}

	public static void onGameEnd(PestControlGame game) {
		games.remove(game);
	}

	public static void sendMessageToWaiting(String message) {
		for (Player p : waiting) {
			p.getClient().queueOutgoingPacket(new SendMessage(message));
		}
	}

	public static void startGame() {
		if (waiting.size() < 2) {
			sendMessageToWaiting("There are not enough players to start.");
			return;
		}
		if (games.size() == 3) {
			sendMessageToWaiting(
					"There are too many active pest control games right now. Please wait for one to finish.");
			return;
		}
		List<Player> toPlay = new LinkedList<Player>();
		int playing = 0;
		Player p;
		while ((playing < 25) && ((p = waiting.poll()) != null)) {
			toPlay.add(p);
			playing++;
		}
		if (waiting.size() > 0) {
			for (Player k : waiting) {
				k.getClient().queueOutgoingPacket(new SendMessage(
						"You couldn't be added to the last game, you've moved up in priority for the next."));
			}
		}
		World.sendGlobalMessage("[Alert: A new game of pest control has just begun]", true);
		games.add(new PestControlGame(toPlay, toPlay.get(0).getIndex() << 2));
	}

	public static void tick() {
		if (waiting.size() > 0) {
			time--;
			if (time == 0 || waiting.size() == 25) {
				startGame();
				if (waiting.size() < 5) {
					time = 400;
				} else {
					time = 200;
				}
			}
		} else if (time != 200) {
			time = 200;
		}
		if (games.size() > 0) {
			for (Iterator<PestControlGame> i = games.iterator(); i.hasNext();) {
				i.next().process();
			}
		}
	}
}
