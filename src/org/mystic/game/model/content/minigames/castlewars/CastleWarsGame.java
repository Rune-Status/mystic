package org.mystic.game.model.content.minigames.castlewars;

import java.util.Collection;
import java.util.Objects;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * The main code that handles the minigame functions
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class CastleWarsGame {

	/**
	 * The collection of active players within the current game
	 */
	private Collection<Player> players = CastleWars.game_participants;

	/**
	 * The maximum amount of time the game can run for
	 */
	private int time = CastleWarsConstants.GAME_TIME;

	/**
	 * The status of if the game has ended or not
	 */
	private boolean ended = false;

	/**
	 * Constructs the game of castle wars
	 * 
	 * @param players
	 *            the players we are putting into the game
	 */
	public CastleWarsGame(Collection<Player> players) {
		this.players = players;
		init();
		World.sendGlobalMessage("@dre@[Castle-Wars] -> A new game has just begun.", true);
		System.out.println("Game started");
	}

	/**
	 * The main method inwhich is called to end the game
	 * 
	 * @param tie
	 *            boolean representing if the game was a tie or not, if the game was
	 *            not a tie, the winner is determined by which team has the highest
	 *            score
	 */
	public void end(boolean tie) {
		if (tie) {
			for (Player p : CastleWars.game_participants) {
				if (p != null) {
					p.send(new SendMessage("The game was a tie! You have recieved 2 tickets!"));
				}
			}
		} else {

		}
		CastleWarsConstants.GAME_ACTIVE = false;

		CastleWars.game_participants.clear();
	}

	/**
	 * Gets the collection and returns the players inside
	 * 
	 * @return
	 */
	private Collection<Player> getPlayers() {
		return players;
	}

	/**
	 * Returns true if the game has ended or not
	 * 
	 * @return
	 */
	public boolean hasEnded() {
		return ended;
	}

	/**
	 * Initalizes the game
	 */
	public void init() {
		for (Player p : getPlayers()) {
			if (Objects.nonNull(p) && p.isActive()) {
				if (Objects.nonNull(p.getTeam())) {
					if (p.getTeam().equals(CastleWarsTeam.SARADOMIN)) {
						p.teleport(CastleWarsConstants.SARADOMIN_BASE);
					} else if (p.getTeam().equals(CastleWarsTeam.ZAMORAK)) {
						p.teleport(CastleWarsConstants.ZAMORAK_BASE);
					}
				}
			}
		}
	}

	/**
	 * Processes the main game of castle wars
	 */
	public void process() {
		time--;
		if (time <= 0) {
			end(false);
			return;
		}
		for (Player p : getPlayers()) {
			// Check if p is not null and make sure they are active
			if (Objects.nonNull(p) && p.isActive()) {
				// Here we will send interfaces, update the score, notify
				// players of
				// what is going on in the game etc.
				p.send(new SendMessage("Active"));
			}
		}
	}

	/**
	 * Removes players and resets/restores the castle wars minigame
	 * 
	 * @param player
	 *            the player we are removing.
	 */
	public void remove(Player player) {
		getPlayers().remove(player);
		if (getPlayers().size() < 2) {

		}
	}

}