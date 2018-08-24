package org.mystic.game.model.content.minigames.castlewars;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;

/**
 * The lobby management for castle wars
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class CastleWarsLobby {

	/**
	 * Represents the time in which a player will spend in the lobby before a game
	 * is started
	 */
	private static int time = CastleWarsConstants.LOBBY_TIME;

	/**
	 * The collection of players waiting in the lobby as the saradomin team
	 */
	public static Collection<Player> saradomin_waiting = new LinkedList<Player>();

	/**
	 * The collection of players waiting in the lobby as the zamorakian team
	 */
	public static Collection<Player> zamorak_waiting = new LinkedList<Player>();

	/**
	 * Manages the sending of the lobby interface and information to the player
	 * inside the lobby waiting room
	 * 
	 * @param player
	 *            the player we are sending the interface and information to
	 */
	public static void sendInterface(Player player) {
		if (Objects.nonNull(player) && Objects.nonNull(player.getTeam())) {
			player.send(new SendWalkableInterface(CastleWarsConstants.LOBBY_INTERFACE_ID));
			if (CastleWarsLobby.zamorak_waiting.size() > 0 && CastleWarsLobby.saradomin_waiting.size() > 0) {
				player.send(
						new SendString("Time till next game: " + time, CastleWarsConstants.LOBBY_TEXT_INTERFACE_ID));
			} else {
				player.send(new SendString("Waiting for players to join the other team.",
						CastleWarsConstants.LOBBY_TEXT_INTERFACE_ID));
			}
		}
	}

	/**
	 * Manages the ticking of the castle wars lobby
	 */
	public static void tick() {
		time--;
		if (time == 0) {
			if (CastleWarsLobby.zamorak_waiting.size() > 0 && CastleWarsLobby.saradomin_waiting.size() > 0) {

			} else {

			}
		}
	}

}
