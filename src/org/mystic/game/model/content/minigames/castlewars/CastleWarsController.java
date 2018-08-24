package org.mystic.game.model.content.minigames.castlewars;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.GenericMinigameController;

/**
 * The controller for the main castle wars game
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 *
 */
public class CastleWarsController extends GenericMinigameController {

	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2) {
		return true;
	}

	@Override
	public boolean canDrink(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canEat(Player paramPlayer) {
		return true;
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
		return true;
	}

	@Override
	public boolean canUsePrayer(Player paramPlayer, int id) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player paramPlayer) {
		return true;
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
	public void onControllerInit(Player paramPlayer) {
	}

	@Override
	public void onDeath(Player paramPlayer) {
	}

	@Override
	public void onDisconnect(Player p) {
		// TODO: remove player from game collections
		p.teleport(CastleWarsConstants.CASTLE_WARS_DEFAULT);
	}

	@Override
	public void tick(Player paramPlayer) {
	}

	@Override
	public String toString() {
		return "CastleWars";
	}

	@Override
	public boolean canLogOut() {
		return false;
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
	}

}