package org.mystic.game.model.content.minigames.castlewars;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.DefaultController;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;

/**
 * The main code regarding the castle wars minigame
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class CastleWars {

	/**
	 * The total collection of {@link Player} within the game
	 */
	public static Collection<Player> game_participants = new LinkedList<Player>();

	/**
	 * The collection of players within the saradomin team
	 */
	public static Collection<Player> saradomin_players = new LinkedList<Player>();

	/**
	 * The collection of players within the zamorak team
	 */
	public static Collection<Player> zamorak_players = new LinkedList<Player>();

	/**
	 * Manages the joining of a team
	 * 
	 * @param player
	 *            the player we are placing into the team
	 * @param team
	 *            the team we are placing the player into
	 */
	public static void joinLobby(Player player, CastleWarsTeam team) {
		if (Objects.nonNull(player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT])
				|| Objects.nonNull(player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT])) {
			DialogueManager.sendStatement(player, "You may not wear a cape or a helmet into castle wars.");
			player.send(new SendMessage("You may not wear a cape or helmet into castle wars."));
			return;
		}
		if (player.getSummoning().hasFamiliar()) {
			player.send(new SendMessage("Please dismiss any active familiar you may have."));
			return;
		}
		if (Objects.nonNull(team)) {
			if (team.equals(CastleWarsTeam.GUTHIX)) {
				if (CastleWarsLobby.zamorak_waiting.size() > CastleWarsLobby.saradomin_waiting.size()) {
					team = CastleWarsTeam.SARADOMIN;
				} else {
					team = CastleWarsTeam.ZAMORAK;
				}
			}
			if (team.equals(CastleWarsTeam.SARADOMIN)) {
				//
				CastleWarsLobby.saradomin_waiting.add(player);
				//
				player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT] = new Item(
						CastleWarsConstants.SARADOMIN_HOOD);
				player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT] = new Item(
						CastleWarsConstants.SARADOMIN_CAPE);

				player.setTeam(CastleWarsTeam.SARADOMIN);

			} else if (team.equals(CastleWarsTeam.ZAMORAK)) {
				//
				CastleWarsLobby.zamorak_waiting.add(player);
				//
				player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT] = new Item(
						CastleWarsConstants.ZAMORAK_HOOD);
				player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT] = new Item(
						CastleWarsConstants.ZAMORAK_CAPE);

				player.setTeam(CastleWarsTeam.ZAMORAK);
			}
			player.getEquipment().update(EquipmentConstants.CAPE_SLOT);
			player.getEquipment().update(EquipmentConstants.HELM_SLOT);
			player.setAppearanceUpdateRequired(true);
			player.getEquipment().calculateBonuses();
			player.setController(new CastleWarsLobbyController());
		}
	}

	/**
	 * Manages the leaving of the pre-game lobby
	 * 
	 * @param player
	 *            the player leaving the lobby
	 * @param team
	 *            the team that the player is leaving
	 */
	public static void exitLobby(Player player, CastleWarsTeam team) {
		if (Objects.nonNull(team) && Objects.nonNull(player.getTeam())) {
			if (CastleWarsLobby.saradomin_waiting.contains(player)) {
				CastleWarsLobby.saradomin_waiting.remove(player);
			} else if (CastleWarsLobby.zamorak_waiting.contains(player)) {
				CastleWarsLobby.zamorak_waiting.remove(player);
			}
			player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT] = null;
			player.getEquipment().getItems()[EquipmentConstants.HELM_SLOT] = null;

			player.getEquipment().update(EquipmentConstants.CAPE_SLOT);
			player.getEquipment().update(EquipmentConstants.HELM_SLOT);

			player.setAppearanceUpdateRequired(true);
			player.getEquipment().calculateBonuses();

			player.send(new SendRemoveInterfaces());
			player.setController(new DefaultController());

			player.send(new SendMessage("You leave the " + player.getTeam().toString().toLowerCase() + " lobby."));
			player.teleport(CastleWarsConstants.CASTLE_WARS_DEFAULT);
		}
	}

}