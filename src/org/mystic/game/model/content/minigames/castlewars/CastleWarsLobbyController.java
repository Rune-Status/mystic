package org.mystic.game.model.content.minigames.castlewars;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.GenericMinigameController;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * The controller for the castle wars lobby
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class CastleWarsLobbyController extends GenericMinigameController {

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2) {
		return false;
	}

	@Override
	public boolean canDrink(Player paramPlayer) {
		return false;
	}

	@Override
	public boolean canEat(Player paramPlayer) {
		return false;
	}

	@Override
	public boolean canEquip(Player paramPlayer, int paramInt1, int paramInt2) {
		return true;
	}

	@Override
	public boolean canUnequip(Player player) {
		return true;
	}

	@Override
	public boolean canDrop(Player player) {
		return false;
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes) {
		return false;
	}

	@Override
	public boolean canUsePrayer(Player paramPlayer, int id) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player paramPlayer) {
		return false;
	}

	@Override
	public Location getRespawnLocation(Player p) {
		return CastleWarsConstants.CASTLE_WARS_DEFAULT;
	}

	@Override
	public boolean isSafe(Player player) {
		return true;
	}

	@Override
	public void onControllerInit(Player player) {
		if (player.getTeam().equals(CastleWarsTeam.SARADOMIN)) {
			player.teleport(CastleWarsConstants.SARADOMIN_LOBBY);
		} else if (player.getTeam().equals(CastleWarsTeam.ZAMORAK)) {
			player.teleport(CastleWarsConstants.ZAMORAK_LOBBY);
		}
		player.send(
				new SendMessage("You have joined the " + player.getTeam().toString().toLowerCase() + " waiting room."));
	}

	@Override
	public void onDeath(Player player) {
		// TODO: remove player from game collections and teleport away
	}

	@Override
	public void onDisconnect(Player player) {
		CastleWars.exitLobby(player, player.getTeam());
	}

	@Override
	public void tick(Player player) {
		CastleWarsLobby.sendInterface(player);
	}

	@Override
	public String toString() {
		return "CastleWarsLobby";
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public boolean canMove(Player p) {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public boolean canTrade() {
		return false;
	}

	@Override
	public void onTeleport(Player p) {
		return;
	}

}